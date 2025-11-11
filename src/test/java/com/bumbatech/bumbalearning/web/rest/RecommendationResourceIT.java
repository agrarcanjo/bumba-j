package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.RecommendationAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.Recommendation;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.RecommendationRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.RecommendationService;
import com.bumbatech.bumbalearning.service.dto.RecommendationDTO;
import com.bumbatech.bumbalearning.service.mapper.RecommendationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecommendationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecommendationResourceIT {

    private static final Instant DEFAULT_RECOMMENDED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECOMMENDED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_WAS_COMPLETED = false;
    private static final Boolean UPDATED_WAS_COMPLETED = true;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recommendations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private RecommendationRepository recommendationRepositoryMock;

    @Autowired
    private RecommendationMapper recommendationMapper;

    @Mock
    private RecommendationService recommendationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecommendationMockMvc;

    private Recommendation recommendation;

    private Recommendation insertedRecommendation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createEntity(EntityManager em) {
        Recommendation recommendation = new Recommendation()
            .recommendedAt(DEFAULT_RECOMMENDED_AT)
            .wasCompleted(DEFAULT_WAS_COMPLETED)
            .reason(DEFAULT_REASON);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        recommendation.setStudent(user);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        recommendation.setLesson(lesson);
        return recommendation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recommendation createUpdatedEntity(EntityManager em) {
        Recommendation updatedRecommendation = new Recommendation()
            .recommendedAt(UPDATED_RECOMMENDED_AT)
            .wasCompleted(UPDATED_WAS_COMPLETED)
            .reason(UPDATED_REASON);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedRecommendation.setStudent(user);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createUpdatedEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        updatedRecommendation.setLesson(lesson);
        return updatedRecommendation;
    }

    @BeforeEach
    void initTest() {
        recommendation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedRecommendation != null) {
            recommendationRepository.delete(insertedRecommendation);
            insertedRecommendation = null;
        }
    }

    @Test
    @Transactional
    void createRecommendation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);
        var returnedRecommendationDTO = om.readValue(
            restRecommendationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recommendationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecommendationDTO.class
        );

        // Validate the Recommendation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecommendation = recommendationMapper.toEntity(returnedRecommendationDTO);
        assertRecommendationUpdatableFieldsEquals(returnedRecommendation, getPersistedRecommendation(returnedRecommendation));

        insertedRecommendation = returnedRecommendation;
    }

    @Test
    @Transactional
    void createRecommendationWithExistingId() throws Exception {
        // Create the Recommendation with an existing ID
        recommendation.setId(1L);
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecommendationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recommendationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRecommendedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recommendation.setRecommendedAt(null);

        // Create the Recommendation, which fails.
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        restRecommendationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recommendationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWasCompletedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recommendation.setWasCompleted(null);

        // Create the Recommendation, which fails.
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        restRecommendationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recommendationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecommendations() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.saveAndFlush(recommendation);

        // Get all the recommendationList
        restRecommendationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recommendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].recommendedAt").value(hasItem(DEFAULT_RECOMMENDED_AT.toString())))
            .andExpect(jsonPath("$.[*].wasCompleted").value(hasItem(DEFAULT_WAS_COMPLETED)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecommendationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(recommendationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecommendationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recommendationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecommendationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recommendationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecommendationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(recommendationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRecommendation() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.saveAndFlush(recommendation);

        // Get the recommendation
        restRecommendationMockMvc
            .perform(get(ENTITY_API_URL_ID, recommendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recommendation.getId().intValue()))
            .andExpect(jsonPath("$.recommendedAt").value(DEFAULT_RECOMMENDED_AT.toString()))
            .andExpect(jsonPath("$.wasCompleted").value(DEFAULT_WAS_COMPLETED))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingRecommendation() throws Exception {
        // Get the recommendation
        restRecommendationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecommendation() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.saveAndFlush(recommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recommendation
        Recommendation updatedRecommendation = recommendationRepository.findById(recommendation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecommendation are not directly saved in db
        em.detach(updatedRecommendation);
        updatedRecommendation.recommendedAt(UPDATED_RECOMMENDED_AT).wasCompleted(UPDATED_WAS_COMPLETED).reason(UPDATED_REASON);
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(updatedRecommendation);

        restRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recommendationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recommendationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecommendationToMatchAllProperties(updatedRecommendation);
    }

    @Test
    @Transactional
    void putNonExistingRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(longCount.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recommendationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(longCount.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(longCount.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recommendationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.saveAndFlush(recommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recommendation using partial update
        Recommendation partialUpdatedRecommendation = new Recommendation();
        partialUpdatedRecommendation.setId(recommendation.getId());

        partialUpdatedRecommendation.recommendedAt(UPDATED_RECOMMENDED_AT).wasCompleted(UPDATED_WAS_COMPLETED);

        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the Recommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecommendationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecommendation, recommendation),
            getPersistedRecommendation(recommendation)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.saveAndFlush(recommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recommendation using partial update
        Recommendation partialUpdatedRecommendation = new Recommendation();
        partialUpdatedRecommendation.setId(recommendation.getId());

        partialUpdatedRecommendation.recommendedAt(UPDATED_RECOMMENDED_AT).wasCompleted(UPDATED_WAS_COMPLETED).reason(UPDATED_REASON);

        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the Recommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecommendationUpdatableFieldsEquals(partialUpdatedRecommendation, getPersistedRecommendation(partialUpdatedRecommendation));
    }

    @Test
    @Transactional
    void patchNonExistingRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(longCount.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recommendationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(longCount.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recommendationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recommendation.setId(longCount.incrementAndGet());

        // Create the Recommendation
        RecommendationDTO recommendationDTO = recommendationMapper.toDto(recommendation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecommendationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recommendationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecommendation() throws Exception {
        // Initialize the database
        insertedRecommendation = recommendationRepository.saveAndFlush(recommendation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recommendation
        restRecommendationMockMvc
            .perform(delete(ENTITY_API_URL_ID, recommendation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recommendationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Recommendation getPersistedRecommendation(Recommendation recommendation) {
        return recommendationRepository.findById(recommendation.getId()).orElseThrow();
    }

    protected void assertPersistedRecommendationToMatchAllProperties(Recommendation expectedRecommendation) {
        assertRecommendationAllPropertiesEquals(expectedRecommendation, getPersistedRecommendation(expectedRecommendation));
    }

    protected void assertPersistedRecommendationToMatchUpdatableProperties(Recommendation expectedRecommendation) {
        assertRecommendationAllUpdatablePropertiesEquals(expectedRecommendation, getPersistedRecommendation(expectedRecommendation));
    }
}
