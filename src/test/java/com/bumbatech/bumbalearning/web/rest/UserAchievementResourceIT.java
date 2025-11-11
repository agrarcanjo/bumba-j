package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.UserAchievementAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.Achievement;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserAchievement;
import com.bumbatech.bumbalearning.repository.UserAchievementRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.UserAchievementService;
import com.bumbatech.bumbalearning.service.dto.UserAchievementDTO;
import com.bumbatech.bumbalearning.service.mapper.UserAchievementMapper;
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
 * Integration tests for the {@link UserAchievementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserAchievementResourceIT {

    private static final Instant DEFAULT_UNLOCKED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UNLOCKED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-achievements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserAchievementRepository userAchievementRepositoryMock;

    @Autowired
    private UserAchievementMapper userAchievementMapper;

    @Mock
    private UserAchievementService userAchievementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAchievementMockMvc;

    private UserAchievement userAchievement;

    private UserAchievement insertedUserAchievement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAchievement createEntity(EntityManager em) {
        UserAchievement userAchievement = new UserAchievement().unlockedAt(DEFAULT_UNLOCKED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        userAchievement.setUser(user);
        // Add required entity
        Achievement achievement;
        if (TestUtil.findAll(em, Achievement.class).isEmpty()) {
            achievement = AchievementResourceIT.createEntity();
            em.persist(achievement);
            em.flush();
        } else {
            achievement = TestUtil.findAll(em, Achievement.class).get(0);
        }
        userAchievement.setAchievement(achievement);
        return userAchievement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAchievement createUpdatedEntity(EntityManager em) {
        UserAchievement updatedUserAchievement = new UserAchievement().unlockedAt(UPDATED_UNLOCKED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedUserAchievement.setUser(user);
        // Add required entity
        Achievement achievement;
        if (TestUtil.findAll(em, Achievement.class).isEmpty()) {
            achievement = AchievementResourceIT.createUpdatedEntity();
            em.persist(achievement);
            em.flush();
        } else {
            achievement = TestUtil.findAll(em, Achievement.class).get(0);
        }
        updatedUserAchievement.setAchievement(achievement);
        return updatedUserAchievement;
    }

    @BeforeEach
    void initTest() {
        userAchievement = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserAchievement != null) {
            userAchievementRepository.delete(insertedUserAchievement);
            insertedUserAchievement = null;
        }
    }

    @Test
    @Transactional
    void createUserAchievement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAchievement
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);
        var returnedUserAchievementDTO = om.readValue(
            restUserAchievementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAchievementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAchievementDTO.class
        );

        // Validate the UserAchievement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserAchievement = userAchievementMapper.toEntity(returnedUserAchievementDTO);
        assertUserAchievementUpdatableFieldsEquals(returnedUserAchievement, getPersistedUserAchievement(returnedUserAchievement));

        insertedUserAchievement = returnedUserAchievement;
    }

    @Test
    @Transactional
    void createUserAchievementWithExistingId() throws Exception {
        // Create the UserAchievement with an existing ID
        userAchievement.setId(1L);
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAchievementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAchievementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUnlockedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAchievement.setUnlockedAt(null);

        // Create the UserAchievement, which fails.
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        restUserAchievementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAchievementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAchievements() throws Exception {
        // Initialize the database
        insertedUserAchievement = userAchievementRepository.saveAndFlush(userAchievement);

        // Get all the userAchievementList
        restUserAchievementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAchievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].unlockedAt").value(hasItem(DEFAULT_UNLOCKED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAchievementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userAchievementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAchievementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userAchievementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAchievementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userAchievementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAchievementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userAchievementRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserAchievement() throws Exception {
        // Initialize the database
        insertedUserAchievement = userAchievementRepository.saveAndFlush(userAchievement);

        // Get the userAchievement
        restUserAchievementMockMvc
            .perform(get(ENTITY_API_URL_ID, userAchievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAchievement.getId().intValue()))
            .andExpect(jsonPath("$.unlockedAt").value(DEFAULT_UNLOCKED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserAchievement() throws Exception {
        // Get the userAchievement
        restUserAchievementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAchievement() throws Exception {
        // Initialize the database
        insertedUserAchievement = userAchievementRepository.saveAndFlush(userAchievement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAchievement
        UserAchievement updatedUserAchievement = userAchievementRepository.findById(userAchievement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAchievement are not directly saved in db
        em.detach(updatedUserAchievement);
        updatedUserAchievement.unlockedAt(UPDATED_UNLOCKED_AT);
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(updatedUserAchievement);

        restUserAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAchievementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAchievementDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAchievementToMatchAllProperties(updatedUserAchievement);
    }

    @Test
    @Transactional
    void putNonExistingUserAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAchievement.setId(longCount.incrementAndGet());

        // Create the UserAchievement
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAchievementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAchievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAchievement.setId(longCount.incrementAndGet());

        // Create the UserAchievement
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAchievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAchievement.setId(longCount.incrementAndGet());

        // Create the UserAchievement
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAchievementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAchievementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAchievementWithPatch() throws Exception {
        // Initialize the database
        insertedUserAchievement = userAchievementRepository.saveAndFlush(userAchievement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAchievement using partial update
        UserAchievement partialUpdatedUserAchievement = new UserAchievement();
        partialUpdatedUserAchievement.setId(userAchievement.getId());

        partialUpdatedUserAchievement.unlockedAt(UPDATED_UNLOCKED_AT);

        restUserAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAchievement))
            )
            .andExpect(status().isOk());

        // Validate the UserAchievement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAchievementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAchievement, userAchievement),
            getPersistedUserAchievement(userAchievement)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAchievementWithPatch() throws Exception {
        // Initialize the database
        insertedUserAchievement = userAchievementRepository.saveAndFlush(userAchievement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAchievement using partial update
        UserAchievement partialUpdatedUserAchievement = new UserAchievement();
        partialUpdatedUserAchievement.setId(userAchievement.getId());

        partialUpdatedUserAchievement.unlockedAt(UPDATED_UNLOCKED_AT);

        restUserAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAchievement))
            )
            .andExpect(status().isOk());

        // Validate the UserAchievement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAchievementUpdatableFieldsEquals(
            partialUpdatedUserAchievement,
            getPersistedUserAchievement(partialUpdatedUserAchievement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAchievement.setId(longCount.incrementAndGet());

        // Create the UserAchievement
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAchievementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAchievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAchievement.setId(longCount.incrementAndGet());

        // Create the UserAchievement
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAchievementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAchievement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAchievement.setId(longCount.incrementAndGet());

        // Create the UserAchievement
        UserAchievementDTO userAchievementDTO = userAchievementMapper.toDto(userAchievement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAchievementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAchievementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAchievement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAchievement() throws Exception {
        // Initialize the database
        insertedUserAchievement = userAchievementRepository.saveAndFlush(userAchievement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAchievement
        restUserAchievementMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAchievement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAchievementRepository.count();
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

    protected UserAchievement getPersistedUserAchievement(UserAchievement userAchievement) {
        return userAchievementRepository.findById(userAchievement.getId()).orElseThrow();
    }

    protected void assertPersistedUserAchievementToMatchAllProperties(UserAchievement expectedUserAchievement) {
        assertUserAchievementAllPropertiesEquals(expectedUserAchievement, getPersistedUserAchievement(expectedUserAchievement));
    }

    protected void assertPersistedUserAchievementToMatchUpdatableProperties(UserAchievement expectedUserAchievement) {
        assertUserAchievementAllUpdatablePropertiesEquals(expectedUserAchievement, getPersistedUserAchievement(expectedUserAchievement));
    }
}
