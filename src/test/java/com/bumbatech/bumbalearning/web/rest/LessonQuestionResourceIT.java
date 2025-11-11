package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.LessonQuestionAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.LessonQuestion;
import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.repository.LessonQuestionRepository;
import com.bumbatech.bumbalearning.service.LessonQuestionService;
import com.bumbatech.bumbalearning.service.dto.LessonQuestionDTO;
import com.bumbatech.bumbalearning.service.mapper.LessonQuestionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link LessonQuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LessonQuestionResourceIT {

    private static final Integer DEFAULT_ORDER_INDEX = 0;
    private static final Integer UPDATED_ORDER_INDEX = 1;

    private static final String ENTITY_API_URL = "/api/lesson-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LessonQuestionRepository lessonQuestionRepository;

    @Mock
    private LessonQuestionRepository lessonQuestionRepositoryMock;

    @Autowired
    private LessonQuestionMapper lessonQuestionMapper;

    @Mock
    private LessonQuestionService lessonQuestionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonQuestionMockMvc;

    private LessonQuestion lessonQuestion;

    private LessonQuestion insertedLessonQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonQuestion createEntity(EntityManager em) {
        LessonQuestion lessonQuestion = new LessonQuestion().orderIndex(DEFAULT_ORDER_INDEX);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        lessonQuestion.setLesson(lesson);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        lessonQuestion.setQuestion(question);
        return lessonQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonQuestion createUpdatedEntity(EntityManager em) {
        LessonQuestion updatedLessonQuestion = new LessonQuestion().orderIndex(UPDATED_ORDER_INDEX);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createUpdatedEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        updatedLessonQuestion.setLesson(lesson);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createUpdatedEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        updatedLessonQuestion.setQuestion(question);
        return updatedLessonQuestion;
    }

    @BeforeEach
    void initTest() {
        lessonQuestion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLessonQuestion != null) {
            lessonQuestionRepository.delete(insertedLessonQuestion);
            insertedLessonQuestion = null;
        }
    }

    @Test
    @Transactional
    void createLessonQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LessonQuestion
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);
        var returnedLessonQuestionDTO = om.readValue(
            restLessonQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonQuestionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LessonQuestionDTO.class
        );

        // Validate the LessonQuestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLessonQuestion = lessonQuestionMapper.toEntity(returnedLessonQuestionDTO);
        assertLessonQuestionUpdatableFieldsEquals(returnedLessonQuestion, getPersistedLessonQuestion(returnedLessonQuestion));

        insertedLessonQuestion = returnedLessonQuestion;
    }

    @Test
    @Transactional
    void createLessonQuestionWithExistingId() throws Exception {
        // Create the LessonQuestion with an existing ID
        lessonQuestion.setId(1L);
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonQuestionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lessonQuestion.setOrderIndex(null);

        // Create the LessonQuestion, which fails.
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        restLessonQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLessonQuestions() throws Exception {
        // Initialize the database
        insertedLessonQuestion = lessonQuestionRepository.saveAndFlush(lessonQuestion);

        // Get all the lessonQuestionList
        restLessonQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonQuestionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(lessonQuestionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lessonQuestionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonQuestionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lessonQuestionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(lessonQuestionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLessonQuestion() throws Exception {
        // Initialize the database
        insertedLessonQuestion = lessonQuestionRepository.saveAndFlush(lessonQuestion);

        // Get the lessonQuestion
        restLessonQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, lessonQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lessonQuestion.getId().intValue()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingLessonQuestion() throws Exception {
        // Get the lessonQuestion
        restLessonQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLessonQuestion() throws Exception {
        // Initialize the database
        insertedLessonQuestion = lessonQuestionRepository.saveAndFlush(lessonQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lessonQuestion
        LessonQuestion updatedLessonQuestion = lessonQuestionRepository.findById(lessonQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLessonQuestion are not directly saved in db
        em.detach(updatedLessonQuestion);
        updatedLessonQuestion.orderIndex(UPDATED_ORDER_INDEX);
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(updatedLessonQuestion);

        restLessonQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lessonQuestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLessonQuestionToMatchAllProperties(updatedLessonQuestion);
    }

    @Test
    @Transactional
    void putNonExistingLessonQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonQuestion.setId(longCount.incrementAndGet());

        // Create the LessonQuestion
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lessonQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLessonQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonQuestion.setId(longCount.incrementAndGet());

        // Create the LessonQuestion
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lessonQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLessonQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonQuestion.setId(longCount.incrementAndGet());

        // Create the LessonQuestion
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedLessonQuestion = lessonQuestionRepository.saveAndFlush(lessonQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lessonQuestion using partial update
        LessonQuestion partialUpdatedLessonQuestion = new LessonQuestion();
        partialUpdatedLessonQuestion.setId(lessonQuestion.getId());

        restLessonQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLessonQuestion))
            )
            .andExpect(status().isOk());

        // Validate the LessonQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLessonQuestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLessonQuestion, lessonQuestion),
            getPersistedLessonQuestion(lessonQuestion)
        );
    }

    @Test
    @Transactional
    void fullUpdateLessonQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedLessonQuestion = lessonQuestionRepository.saveAndFlush(lessonQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lessonQuestion using partial update
        LessonQuestion partialUpdatedLessonQuestion = new LessonQuestion();
        partialUpdatedLessonQuestion.setId(lessonQuestion.getId());

        partialUpdatedLessonQuestion.orderIndex(UPDATED_ORDER_INDEX);

        restLessonQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLessonQuestion))
            )
            .andExpect(status().isOk());

        // Validate the LessonQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLessonQuestionUpdatableFieldsEquals(partialUpdatedLessonQuestion, getPersistedLessonQuestion(partialUpdatedLessonQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingLessonQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonQuestion.setId(longCount.incrementAndGet());

        // Create the LessonQuestion
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonQuestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lessonQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLessonQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonQuestion.setId(longCount.incrementAndGet());

        // Create the LessonQuestion
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lessonQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLessonQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonQuestion.setId(longCount.incrementAndGet());

        // Create the LessonQuestion
        LessonQuestionDTO lessonQuestionDTO = lessonQuestionMapper.toDto(lessonQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lessonQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLessonQuestion() throws Exception {
        // Initialize the database
        insertedLessonQuestion = lessonQuestionRepository.saveAndFlush(lessonQuestion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lessonQuestion
        restLessonQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, lessonQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lessonQuestionRepository.count();
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

    protected LessonQuestion getPersistedLessonQuestion(LessonQuestion lessonQuestion) {
        return lessonQuestionRepository.findById(lessonQuestion.getId()).orElseThrow();
    }

    protected void assertPersistedLessonQuestionToMatchAllProperties(LessonQuestion expectedLessonQuestion) {
        assertLessonQuestionAllPropertiesEquals(expectedLessonQuestion, getPersistedLessonQuestion(expectedLessonQuestion));
    }

    protected void assertPersistedLessonQuestionToMatchUpdatableProperties(LessonQuestion expectedLessonQuestion) {
        assertLessonQuestionAllUpdatablePropertiesEquals(expectedLessonQuestion, getPersistedLessonQuestion(expectedLessonQuestion));
    }
}
