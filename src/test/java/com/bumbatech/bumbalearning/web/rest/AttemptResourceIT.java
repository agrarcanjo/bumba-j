package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.AttemptAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.Attempt;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.AttemptRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.AttemptService;
import com.bumbatech.bumbalearning.service.dto.AttemptDTO;
import com.bumbatech.bumbalearning.service.mapper.AttemptMapper;
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
 * Integration tests for the {@link AttemptResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AttemptResourceIT {

    private static final Boolean DEFAULT_IS_CORRECT = false;
    private static final Boolean UPDATED_IS_CORRECT = true;

    private static final Integer DEFAULT_TIME_SPENT_SECONDS = 0;
    private static final Integer UPDATED_TIME_SPENT_SECONDS = 1;
    private static final Integer SMALLER_TIME_SPENT_SECONDS = 0 - 1;

    private static final Instant DEFAULT_ATTEMPTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ATTEMPTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ANSWER = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attempts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private AttemptRepository attemptRepositoryMock;

    @Autowired
    private AttemptMapper attemptMapper;

    @Mock
    private AttemptService attemptServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttemptMockMvc;

    private Attempt attempt;

    private Attempt insertedAttempt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attempt createEntity(EntityManager em) {
        Attempt attempt = new Attempt()
            .isCorrect(DEFAULT_IS_CORRECT)
            .timeSpentSeconds(DEFAULT_TIME_SPENT_SECONDS)
            .attemptedAt(DEFAULT_ATTEMPTED_AT)
            .answer(DEFAULT_ANSWER)
            .metadata(DEFAULT_METADATA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        attempt.setStudent(user);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        attempt.setQuestion(question);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        attempt.setLesson(lesson);
        return attempt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attempt createUpdatedEntity(EntityManager em) {
        Attempt updatedAttempt = new Attempt()
            .isCorrect(UPDATED_IS_CORRECT)
            .timeSpentSeconds(UPDATED_TIME_SPENT_SECONDS)
            .attemptedAt(UPDATED_ATTEMPTED_AT)
            .answer(UPDATED_ANSWER)
            .metadata(UPDATED_METADATA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedAttempt.setStudent(user);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createUpdatedEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        updatedAttempt.setQuestion(question);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createUpdatedEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        updatedAttempt.setLesson(lesson);
        return updatedAttempt;
    }

    @BeforeEach
    void initTest() {
        attempt = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAttempt != null) {
            attemptRepository.delete(insertedAttempt);
            insertedAttempt = null;
        }
    }

    @Test
    @Transactional
    void createAttempt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);
        var returnedAttemptDTO = om.readValue(
            restAttemptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AttemptDTO.class
        );

        // Validate the Attempt in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAttempt = attemptMapper.toEntity(returnedAttemptDTO);
        assertAttemptUpdatableFieldsEquals(returnedAttempt, getPersistedAttempt(returnedAttempt));

        insertedAttempt = returnedAttempt;
    }

    @Test
    @Transactional
    void createAttemptWithExistingId() throws Exception {
        // Create the Attempt with an existing ID
        attempt.setId(1L);
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsCorrectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attempt.setIsCorrect(null);

        // Create the Attempt, which fails.
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeSpentSecondsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attempt.setTimeSpentSeconds(null);

        // Create the Attempt, which fails.
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttemptedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        attempt.setAttemptedAt(null);

        // Create the Attempt, which fails.
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttempts() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].isCorrect").value(hasItem(DEFAULT_IS_CORRECT)))
            .andExpect(jsonPath("$.[*].timeSpentSeconds").value(hasItem(DEFAULT_TIME_SPENT_SECONDS)))
            .andExpect(jsonPath("$.[*].attemptedAt").value(hasItem(DEFAULT_ATTEMPTED_AT.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttemptsWithEagerRelationshipsIsEnabled() throws Exception {
        when(attemptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttemptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(attemptServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttemptsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(attemptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttemptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(attemptRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAttempt() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get the attempt
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL_ID, attempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attempt.getId().intValue()))
            .andExpect(jsonPath("$.isCorrect").value(DEFAULT_IS_CORRECT))
            .andExpect(jsonPath("$.timeSpentSeconds").value(DEFAULT_TIME_SPENT_SECONDS))
            .andExpect(jsonPath("$.attemptedAt").value(DEFAULT_ATTEMPTED_AT.toString()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA));
    }

    @Test
    @Transactional
    void getAttemptsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        Long id = attempt.getId();

        defaultAttemptFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAttemptFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAttemptFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAttemptsByIsCorrectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where isCorrect equals to
        defaultAttemptFiltering("isCorrect.equals=" + DEFAULT_IS_CORRECT, "isCorrect.equals=" + UPDATED_IS_CORRECT);
    }

    @Test
    @Transactional
    void getAllAttemptsByIsCorrectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where isCorrect in
        defaultAttemptFiltering("isCorrect.in=" + DEFAULT_IS_CORRECT + "," + UPDATED_IS_CORRECT, "isCorrect.in=" + UPDATED_IS_CORRECT);
    }

    @Test
    @Transactional
    void getAllAttemptsByIsCorrectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where isCorrect is not null
        defaultAttemptFiltering("isCorrect.specified=true", "isCorrect.specified=false");
    }

    @Test
    @Transactional
    void getAllAttemptsByTimeSpentSecondsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where timeSpentSeconds equals to
        defaultAttemptFiltering(
            "timeSpentSeconds.equals=" + DEFAULT_TIME_SPENT_SECONDS,
            "timeSpentSeconds.equals=" + UPDATED_TIME_SPENT_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllAttemptsByTimeSpentSecondsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where timeSpentSeconds in
        defaultAttemptFiltering(
            "timeSpentSeconds.in=" + DEFAULT_TIME_SPENT_SECONDS + "," + UPDATED_TIME_SPENT_SECONDS,
            "timeSpentSeconds.in=" + UPDATED_TIME_SPENT_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllAttemptsByTimeSpentSecondsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where timeSpentSeconds is not null
        defaultAttemptFiltering("timeSpentSeconds.specified=true", "timeSpentSeconds.specified=false");
    }

    @Test
    @Transactional
    void getAllAttemptsByTimeSpentSecondsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where timeSpentSeconds is greater than or equal to
        defaultAttemptFiltering(
            "timeSpentSeconds.greaterThanOrEqual=" + DEFAULT_TIME_SPENT_SECONDS,
            "timeSpentSeconds.greaterThanOrEqual=" + UPDATED_TIME_SPENT_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllAttemptsByTimeSpentSecondsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where timeSpentSeconds is less than or equal to
        defaultAttemptFiltering(
            "timeSpentSeconds.lessThanOrEqual=" + DEFAULT_TIME_SPENT_SECONDS,
            "timeSpentSeconds.lessThanOrEqual=" + SMALLER_TIME_SPENT_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllAttemptsByTimeSpentSecondsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where timeSpentSeconds is less than
        defaultAttemptFiltering(
            "timeSpentSeconds.lessThan=" + UPDATED_TIME_SPENT_SECONDS,
            "timeSpentSeconds.lessThan=" + DEFAULT_TIME_SPENT_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllAttemptsByTimeSpentSecondsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where timeSpentSeconds is greater than
        defaultAttemptFiltering(
            "timeSpentSeconds.greaterThan=" + SMALLER_TIME_SPENT_SECONDS,
            "timeSpentSeconds.greaterThan=" + DEFAULT_TIME_SPENT_SECONDS
        );
    }

    @Test
    @Transactional
    void getAllAttemptsByAttemptedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where attemptedAt equals to
        defaultAttemptFiltering("attemptedAt.equals=" + DEFAULT_ATTEMPTED_AT, "attemptedAt.equals=" + UPDATED_ATTEMPTED_AT);
    }

    @Test
    @Transactional
    void getAllAttemptsByAttemptedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where attemptedAt in
        defaultAttemptFiltering(
            "attemptedAt.in=" + DEFAULT_ATTEMPTED_AT + "," + UPDATED_ATTEMPTED_AT,
            "attemptedAt.in=" + UPDATED_ATTEMPTED_AT
        );
    }

    @Test
    @Transactional
    void getAllAttemptsByAttemptedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList where attemptedAt is not null
        defaultAttemptFiltering("attemptedAt.specified=true", "attemptedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAttemptsByStudentIsEqualToSomething() throws Exception {
        User student;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            attemptRepository.saveAndFlush(attempt);
            student = UserResourceIT.createEntity();
        } else {
            student = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(student);
        em.flush();
        attempt.setStudent(student);
        attemptRepository.saveAndFlush(attempt);
        Long studentId = student.getId();
        // Get all the attemptList where student equals to studentId
        defaultAttemptShouldBeFound("studentId.equals=" + studentId);

        // Get all the attemptList where student equals to (studentId + 1)
        defaultAttemptShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    @Test
    @Transactional
    void getAllAttemptsByQuestionIsEqualToSomething() throws Exception {
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            attemptRepository.saveAndFlush(attempt);
            question = QuestionResourceIT.createEntity(em);
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        em.persist(question);
        em.flush();
        attempt.setQuestion(question);
        attemptRepository.saveAndFlush(attempt);
        Long questionId = question.getId();
        // Get all the attemptList where question equals to questionId
        defaultAttemptShouldBeFound("questionId.equals=" + questionId);

        // Get all the attemptList where question equals to (questionId + 1)
        defaultAttemptShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    @Test
    @Transactional
    void getAllAttemptsByLessonIsEqualToSomething() throws Exception {
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            attemptRepository.saveAndFlush(attempt);
            lesson = LessonResourceIT.createEntity(em);
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        em.persist(lesson);
        em.flush();
        attempt.setLesson(lesson);
        attemptRepository.saveAndFlush(attempt);
        Long lessonId = lesson.getId();
        // Get all the attemptList where lesson equals to lessonId
        defaultAttemptShouldBeFound("lessonId.equals=" + lessonId);

        // Get all the attemptList where lesson equals to (lessonId + 1)
        defaultAttemptShouldNotBeFound("lessonId.equals=" + (lessonId + 1));
    }

    private void defaultAttemptFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAttemptShouldBeFound(shouldBeFound);
        defaultAttemptShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttemptShouldBeFound(String filter) throws Exception {
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].isCorrect").value(hasItem(DEFAULT_IS_CORRECT)))
            .andExpect(jsonPath("$.[*].timeSpentSeconds").value(hasItem(DEFAULT_TIME_SPENT_SECONDS)))
            .andExpect(jsonPath("$.[*].attemptedAt").value(hasItem(DEFAULT_ATTEMPTED_AT.toString())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));

        // Check, that the count call also returns 1
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttemptShouldNotBeFound(String filter) throws Exception {
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAttempt() throws Exception {
        // Get the attempt
        restAttemptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttempt() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attempt
        Attempt updatedAttempt = attemptRepository.findById(attempt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAttempt are not directly saved in db
        em.detach(updatedAttempt);
        updatedAttempt
            .isCorrect(UPDATED_IS_CORRECT)
            .timeSpentSeconds(UPDATED_TIME_SPENT_SECONDS)
            .attemptedAt(UPDATED_ATTEMPTED_AT)
            .answer(UPDATED_ANSWER)
            .metadata(UPDATED_METADATA);
        AttemptDTO attemptDTO = attemptMapper.toDto(updatedAttempt);

        restAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attemptDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO))
            )
            .andExpect(status().isOk());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAttemptToMatchAllProperties(updatedAttempt);
    }

    @Test
    @Transactional
    void putNonExistingAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attempt.setId(longCount.incrementAndGet());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attemptDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attempt.setId(longCount.incrementAndGet());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attempt.setId(longCount.incrementAndGet());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(attemptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttemptWithPatch() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attempt using partial update
        Attempt partialUpdatedAttempt = new Attempt();
        partialUpdatedAttempt.setId(attempt.getId());

        partialUpdatedAttempt
            .isCorrect(UPDATED_IS_CORRECT)
            .timeSpentSeconds(UPDATED_TIME_SPENT_SECONDS)
            .attemptedAt(UPDATED_ATTEMPTED_AT)
            .answer(UPDATED_ANSWER)
            .metadata(UPDATED_METADATA);

        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttempt))
            )
            .andExpect(status().isOk());

        // Validate the Attempt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttemptUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAttempt, attempt), getPersistedAttempt(attempt));
    }

    @Test
    @Transactional
    void fullUpdateAttemptWithPatch() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the attempt using partial update
        Attempt partialUpdatedAttempt = new Attempt();
        partialUpdatedAttempt.setId(attempt.getId());

        partialUpdatedAttempt
            .isCorrect(UPDATED_IS_CORRECT)
            .timeSpentSeconds(UPDATED_TIME_SPENT_SECONDS)
            .attemptedAt(UPDATED_ATTEMPTED_AT)
            .answer(UPDATED_ANSWER)
            .metadata(UPDATED_METADATA);

        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAttempt))
            )
            .andExpect(status().isOk());

        // Validate the Attempt in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAttemptUpdatableFieldsEquals(partialUpdatedAttempt, getPersistedAttempt(partialUpdatedAttempt));
    }

    @Test
    @Transactional
    void patchNonExistingAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attempt.setId(longCount.incrementAndGet());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attemptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attempt.setId(longCount.incrementAndGet());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttempt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        attempt.setId(longCount.incrementAndGet());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(attemptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attempt in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttempt() throws Exception {
        // Initialize the database
        insertedAttempt = attemptRepository.saveAndFlush(attempt);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the attempt
        restAttemptMockMvc
            .perform(delete(ENTITY_API_URL_ID, attempt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return attemptRepository.count();
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

    protected Attempt getPersistedAttempt(Attempt attempt) {
        return attemptRepository.findById(attempt.getId()).orElseThrow();
    }

    protected void assertPersistedAttemptToMatchAllProperties(Attempt expectedAttempt) {
        assertAttemptAllPropertiesEquals(expectedAttempt, getPersistedAttempt(expectedAttempt));
    }

    protected void assertPersistedAttemptToMatchUpdatableProperties(Attempt expectedAttempt) {
        assertAttemptAllUpdatablePropertiesEquals(expectedAttempt, getPersistedAttempt(expectedAttempt));
    }
}
