package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.StudentLessonProgressAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.LessonAssignment;
import com.bumbatech.bumbalearning.domain.StudentLessonProgress;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import com.bumbatech.bumbalearning.repository.StudentLessonProgressRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.StudentLessonProgressService;
import com.bumbatech.bumbalearning.service.dto.StudentLessonProgressDTO;
import com.bumbatech.bumbalearning.service.mapper.StudentLessonProgressMapper;
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
 * Integration tests for the {@link StudentLessonProgressResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StudentLessonProgressResourceIT {

    private static final LessonStatus DEFAULT_STATUS = LessonStatus.NOT_STARTED;
    private static final LessonStatus UPDATED_STATUS = LessonStatus.IN_PROGRESS;

    private static final Integer DEFAULT_SCORE = 0;
    private static final Integer UPDATED_SCORE = 1;
    private static final Integer SMALLER_SCORE = 0 - 1;

    private static final Integer DEFAULT_XP_EARNED = 0;
    private static final Integer UPDATED_XP_EARNED = 1;
    private static final Integer SMALLER_XP_EARNED = 0 - 1;

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_LATE = false;
    private static final Boolean UPDATED_IS_LATE = true;

    private static final String ENTITY_API_URL = "/api/student-lesson-progresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StudentLessonProgressRepository studentLessonProgressRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private StudentLessonProgressRepository studentLessonProgressRepositoryMock;

    @Autowired
    private StudentLessonProgressMapper studentLessonProgressMapper;

    @Mock
    private StudentLessonProgressService studentLessonProgressServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentLessonProgressMockMvc;

    private StudentLessonProgress studentLessonProgress;

    private StudentLessonProgress insertedStudentLessonProgress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentLessonProgress createEntity(EntityManager em) {
        StudentLessonProgress studentLessonProgress = new StudentLessonProgress()
            .status(DEFAULT_STATUS)
            .score(DEFAULT_SCORE)
            .xpEarned(DEFAULT_XP_EARNED)
            .completedAt(DEFAULT_COMPLETED_AT)
            .isLate(DEFAULT_IS_LATE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        studentLessonProgress.setStudent(user);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        studentLessonProgress.setLesson(lesson);
        return studentLessonProgress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentLessonProgress createUpdatedEntity(EntityManager em) {
        StudentLessonProgress updatedStudentLessonProgress = new StudentLessonProgress()
            .status(UPDATED_STATUS)
            .score(UPDATED_SCORE)
            .xpEarned(UPDATED_XP_EARNED)
            .completedAt(UPDATED_COMPLETED_AT)
            .isLate(UPDATED_IS_LATE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedStudentLessonProgress.setStudent(user);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createUpdatedEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        updatedStudentLessonProgress.setLesson(lesson);
        return updatedStudentLessonProgress;
    }

    @BeforeEach
    void initTest() {
        studentLessonProgress = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedStudentLessonProgress != null) {
            studentLessonProgressRepository.delete(insertedStudentLessonProgress);
            insertedStudentLessonProgress = null;
        }
    }

    @Test
    @Transactional
    void createStudentLessonProgress() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StudentLessonProgress
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);
        var returnedStudentLessonProgressDTO = om.readValue(
            restStudentLessonProgressMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentLessonProgressDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StudentLessonProgressDTO.class
        );

        // Validate the StudentLessonProgress in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStudentLessonProgress = studentLessonProgressMapper.toEntity(returnedStudentLessonProgressDTO);
        assertStudentLessonProgressUpdatableFieldsEquals(
            returnedStudentLessonProgress,
            getPersistedStudentLessonProgress(returnedStudentLessonProgress)
        );

        insertedStudentLessonProgress = returnedStudentLessonProgress;
    }

    @Test
    @Transactional
    void createStudentLessonProgressWithExistingId() throws Exception {
        // Create the StudentLessonProgress with an existing ID
        studentLessonProgress.setId(1L);
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentLessonProgressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentLessonProgressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        studentLessonProgress.setStatus(null);

        // Create the StudentLessonProgress, which fails.
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        restStudentLessonProgressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentLessonProgressDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgresses() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList
        restStudentLessonProgressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentLessonProgress.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].xpEarned").value(hasItem(DEFAULT_XP_EARNED)))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].isLate").value(hasItem(DEFAULT_IS_LATE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudentLessonProgressesWithEagerRelationshipsIsEnabled() throws Exception {
        when(studentLessonProgressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentLessonProgressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(studentLessonProgressServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStudentLessonProgressesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(studentLessonProgressServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentLessonProgressMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(studentLessonProgressRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getStudentLessonProgress() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get the studentLessonProgress
        restStudentLessonProgressMockMvc
            .perform(get(ENTITY_API_URL_ID, studentLessonProgress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentLessonProgress.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.xpEarned").value(DEFAULT_XP_EARNED))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.isLate").value(DEFAULT_IS_LATE));
    }

    @Test
    @Transactional
    void getStudentLessonProgressesByIdFiltering() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        Long id = studentLessonProgress.getId();

        defaultStudentLessonProgressFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStudentLessonProgressFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStudentLessonProgressFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where status equals to
        defaultStudentLessonProgressFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where status in
        defaultStudentLessonProgressFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where status is not null
        defaultStudentLessonProgressFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where score equals to
        defaultStudentLessonProgressFiltering("score.equals=" + DEFAULT_SCORE, "score.equals=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByScoreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where score in
        defaultStudentLessonProgressFiltering("score.in=" + DEFAULT_SCORE + "," + UPDATED_SCORE, "score.in=" + UPDATED_SCORE);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where score is not null
        defaultStudentLessonProgressFiltering("score.specified=true", "score.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where score is greater than or equal to
        defaultStudentLessonProgressFiltering(
            "score.greaterThanOrEqual=" + DEFAULT_SCORE,
            "score.greaterThanOrEqual=" + (DEFAULT_SCORE + 1)
        );
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where score is less than or equal to
        defaultStudentLessonProgressFiltering("score.lessThanOrEqual=" + DEFAULT_SCORE, "score.lessThanOrEqual=" + SMALLER_SCORE);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where score is less than
        defaultStudentLessonProgressFiltering("score.lessThan=" + (DEFAULT_SCORE + 1), "score.lessThan=" + DEFAULT_SCORE);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where score is greater than
        defaultStudentLessonProgressFiltering("score.greaterThan=" + SMALLER_SCORE, "score.greaterThan=" + DEFAULT_SCORE);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByXpEarnedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where xpEarned equals to
        defaultStudentLessonProgressFiltering("xpEarned.equals=" + DEFAULT_XP_EARNED, "xpEarned.equals=" + UPDATED_XP_EARNED);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByXpEarnedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where xpEarned in
        defaultStudentLessonProgressFiltering(
            "xpEarned.in=" + DEFAULT_XP_EARNED + "," + UPDATED_XP_EARNED,
            "xpEarned.in=" + UPDATED_XP_EARNED
        );
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByXpEarnedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where xpEarned is not null
        defaultStudentLessonProgressFiltering("xpEarned.specified=true", "xpEarned.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByXpEarnedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where xpEarned is greater than or equal to
        defaultStudentLessonProgressFiltering(
            "xpEarned.greaterThanOrEqual=" + DEFAULT_XP_EARNED,
            "xpEarned.greaterThanOrEqual=" + UPDATED_XP_EARNED
        );
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByXpEarnedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where xpEarned is less than or equal to
        defaultStudentLessonProgressFiltering(
            "xpEarned.lessThanOrEqual=" + DEFAULT_XP_EARNED,
            "xpEarned.lessThanOrEqual=" + SMALLER_XP_EARNED
        );
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByXpEarnedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where xpEarned is less than
        defaultStudentLessonProgressFiltering("xpEarned.lessThan=" + UPDATED_XP_EARNED, "xpEarned.lessThan=" + DEFAULT_XP_EARNED);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByXpEarnedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where xpEarned is greater than
        defaultStudentLessonProgressFiltering("xpEarned.greaterThan=" + SMALLER_XP_EARNED, "xpEarned.greaterThan=" + DEFAULT_XP_EARNED);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where completedAt equals to
        defaultStudentLessonProgressFiltering("completedAt.equals=" + DEFAULT_COMPLETED_AT, "completedAt.equals=" + UPDATED_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where completedAt in
        defaultStudentLessonProgressFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where completedAt is not null
        defaultStudentLessonProgressFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByIsLateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where isLate equals to
        defaultStudentLessonProgressFiltering("isLate.equals=" + DEFAULT_IS_LATE, "isLate.equals=" + UPDATED_IS_LATE);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByIsLateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where isLate in
        defaultStudentLessonProgressFiltering("isLate.in=" + DEFAULT_IS_LATE + "," + UPDATED_IS_LATE, "isLate.in=" + UPDATED_IS_LATE);
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByIsLateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        // Get all the studentLessonProgressList where isLate is not null
        defaultStudentLessonProgressFiltering("isLate.specified=true", "isLate.specified=false");
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByStudentIsEqualToSomething() throws Exception {
        User student;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            studentLessonProgressRepository.saveAndFlush(studentLessonProgress);
            student = UserResourceIT.createEntity();
        } else {
            student = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(student);
        em.flush();
        studentLessonProgress.setStudent(student);
        studentLessonProgressRepository.saveAndFlush(studentLessonProgress);
        Long studentId = student.getId();
        // Get all the studentLessonProgressList where student equals to studentId
        defaultStudentLessonProgressShouldBeFound("studentId.equals=" + studentId);

        // Get all the studentLessonProgressList where student equals to (studentId + 1)
        defaultStudentLessonProgressShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByLessonIsEqualToSomething() throws Exception {
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            studentLessonProgressRepository.saveAndFlush(studentLessonProgress);
            lesson = LessonResourceIT.createEntity(em);
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        em.persist(lesson);
        em.flush();
        studentLessonProgress.setLesson(lesson);
        studentLessonProgressRepository.saveAndFlush(studentLessonProgress);
        Long lessonId = lesson.getId();
        // Get all the studentLessonProgressList where lesson equals to lessonId
        defaultStudentLessonProgressShouldBeFound("lessonId.equals=" + lessonId);

        // Get all the studentLessonProgressList where lesson equals to (lessonId + 1)
        defaultStudentLessonProgressShouldNotBeFound("lessonId.equals=" + (lessonId + 1));
    }

    @Test
    @Transactional
    void getAllStudentLessonProgressesByAssignmentIsEqualToSomething() throws Exception {
        LessonAssignment assignment;
        if (TestUtil.findAll(em, LessonAssignment.class).isEmpty()) {
            studentLessonProgressRepository.saveAndFlush(studentLessonProgress);
            assignment = LessonAssignmentResourceIT.createEntity(em);
        } else {
            assignment = TestUtil.findAll(em, LessonAssignment.class).get(0);
        }
        em.persist(assignment);
        em.flush();
        studentLessonProgress.setAssignment(assignment);
        studentLessonProgressRepository.saveAndFlush(studentLessonProgress);
        Long assignmentId = assignment.getId();
        // Get all the studentLessonProgressList where assignment equals to assignmentId
        defaultStudentLessonProgressShouldBeFound("assignmentId.equals=" + assignmentId);

        // Get all the studentLessonProgressList where assignment equals to (assignmentId + 1)
        defaultStudentLessonProgressShouldNotBeFound("assignmentId.equals=" + (assignmentId + 1));
    }

    private void defaultStudentLessonProgressFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStudentLessonProgressShouldBeFound(shouldBeFound);
        defaultStudentLessonProgressShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentLessonProgressShouldBeFound(String filter) throws Exception {
        restStudentLessonProgressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentLessonProgress.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].xpEarned").value(hasItem(DEFAULT_XP_EARNED)))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].isLate").value(hasItem(DEFAULT_IS_LATE)));

        // Check, that the count call also returns 1
        restStudentLessonProgressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentLessonProgressShouldNotBeFound(String filter) throws Exception {
        restStudentLessonProgressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentLessonProgressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStudentLessonProgress() throws Exception {
        // Get the studentLessonProgress
        restStudentLessonProgressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudentLessonProgress() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentLessonProgress
        StudentLessonProgress updatedStudentLessonProgress = studentLessonProgressRepository
            .findById(studentLessonProgress.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedStudentLessonProgress are not directly saved in db
        em.detach(updatedStudentLessonProgress);
        updatedStudentLessonProgress
            .status(UPDATED_STATUS)
            .score(UPDATED_SCORE)
            .xpEarned(UPDATED_XP_EARNED)
            .completedAt(UPDATED_COMPLETED_AT)
            .isLate(UPDATED_IS_LATE);
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(updatedStudentLessonProgress);

        restStudentLessonProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentLessonProgressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentLessonProgressDTO))
            )
            .andExpect(status().isOk());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStudentLessonProgressToMatchAllProperties(updatedStudentLessonProgress);
    }

    @Test
    @Transactional
    void putNonExistingStudentLessonProgress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentLessonProgress.setId(longCount.incrementAndGet());

        // Create the StudentLessonProgress
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentLessonProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentLessonProgressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentLessonProgressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentLessonProgress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentLessonProgress.setId(longCount.incrementAndGet());

        // Create the StudentLessonProgress
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentLessonProgressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(studentLessonProgressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentLessonProgress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentLessonProgress.setId(longCount.incrementAndGet());

        // Create the StudentLessonProgress
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentLessonProgressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(studentLessonProgressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentLessonProgressWithPatch() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentLessonProgress using partial update
        StudentLessonProgress partialUpdatedStudentLessonProgress = new StudentLessonProgress();
        partialUpdatedStudentLessonProgress.setId(studentLessonProgress.getId());

        restStudentLessonProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentLessonProgress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentLessonProgress))
            )
            .andExpect(status().isOk());

        // Validate the StudentLessonProgress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentLessonProgressUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStudentLessonProgress, studentLessonProgress),
            getPersistedStudentLessonProgress(studentLessonProgress)
        );
    }

    @Test
    @Transactional
    void fullUpdateStudentLessonProgressWithPatch() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the studentLessonProgress using partial update
        StudentLessonProgress partialUpdatedStudentLessonProgress = new StudentLessonProgress();
        partialUpdatedStudentLessonProgress.setId(studentLessonProgress.getId());

        partialUpdatedStudentLessonProgress
            .status(UPDATED_STATUS)
            .score(UPDATED_SCORE)
            .xpEarned(UPDATED_XP_EARNED)
            .completedAt(UPDATED_COMPLETED_AT)
            .isLate(UPDATED_IS_LATE);

        restStudentLessonProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentLessonProgress.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStudentLessonProgress))
            )
            .andExpect(status().isOk());

        // Validate the StudentLessonProgress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStudentLessonProgressUpdatableFieldsEquals(
            partialUpdatedStudentLessonProgress,
            getPersistedStudentLessonProgress(partialUpdatedStudentLessonProgress)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStudentLessonProgress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentLessonProgress.setId(longCount.incrementAndGet());

        // Create the StudentLessonProgress
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentLessonProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentLessonProgressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentLessonProgressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentLessonProgress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentLessonProgress.setId(longCount.incrementAndGet());

        // Create the StudentLessonProgress
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentLessonProgressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(studentLessonProgressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentLessonProgress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        studentLessonProgress.setId(longCount.incrementAndGet());

        // Create the StudentLessonProgress
        StudentLessonProgressDTO studentLessonProgressDTO = studentLessonProgressMapper.toDto(studentLessonProgress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentLessonProgressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(studentLessonProgressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentLessonProgress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentLessonProgress() throws Exception {
        // Initialize the database
        insertedStudentLessonProgress = studentLessonProgressRepository.saveAndFlush(studentLessonProgress);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the studentLessonProgress
        restStudentLessonProgressMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentLessonProgress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return studentLessonProgressRepository.count();
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

    protected StudentLessonProgress getPersistedStudentLessonProgress(StudentLessonProgress studentLessonProgress) {
        return studentLessonProgressRepository.findById(studentLessonProgress.getId()).orElseThrow();
    }

    protected void assertPersistedStudentLessonProgressToMatchAllProperties(StudentLessonProgress expectedStudentLessonProgress) {
        assertStudentLessonProgressAllPropertiesEquals(
            expectedStudentLessonProgress,
            getPersistedStudentLessonProgress(expectedStudentLessonProgress)
        );
    }

    protected void assertPersistedStudentLessonProgressToMatchUpdatableProperties(StudentLessonProgress expectedStudentLessonProgress) {
        assertStudentLessonProgressAllUpdatablePropertiesEquals(
            expectedStudentLessonProgress,
            getPersistedStudentLessonProgress(expectedStudentLessonProgress)
        );
    }
}
