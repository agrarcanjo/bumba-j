package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.LessonAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.Topic;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import com.bumbatech.bumbalearning.repository.LessonRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.LessonService;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.mapper.LessonMapper;
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
 * Integration tests for the {@link LessonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LessonResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.ENGLISH;
    private static final Language UPDATED_LANGUAGE = Language.SPANISH;

    private static final DifficultyLevel DEFAULT_LEVEL = DifficultyLevel.EASY;
    private static final DifficultyLevel UPDATED_LEVEL = DifficultyLevel.INTERMEDIATE;

    private static final Integer DEFAULT_XP_REWARD = 0;
    private static final Integer UPDATED_XP_REWARD = 1;
    private static final Integer SMALLER_XP_REWARD = 0 - 1;

    private static final Integer DEFAULT_PASS_THRESHOLD = 0;
    private static final Integer UPDATED_PASS_THRESHOLD = 1;
    private static final Integer SMALLER_PASS_THRESHOLD = 0 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lessons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private LessonRepository lessonRepositoryMock;

    @Autowired
    private LessonMapper lessonMapper;

    @Mock
    private LessonService lessonServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonMockMvc;

    private Lesson lesson;

    private Lesson insertedLesson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lesson createEntity(EntityManager em) {
        Lesson lesson = new Lesson()
            .title(DEFAULT_TITLE)
            .language(DEFAULT_LANGUAGE)
            .level(DEFAULT_LEVEL)
            .xpReward(DEFAULT_XP_REWARD)
            .passThreshold(DEFAULT_PASS_THRESHOLD)
            .createdAt(DEFAULT_CREATED_AT)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createEntity();
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        lesson.setTopic(topic);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        lesson.setCreatedBy(user);
        return lesson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lesson createUpdatedEntity(EntityManager em) {
        Lesson updatedLesson = new Lesson()
            .title(UPDATED_TITLE)
            .language(UPDATED_LANGUAGE)
            .level(UPDATED_LEVEL)
            .xpReward(UPDATED_XP_REWARD)
            .passThreshold(UPDATED_PASS_THRESHOLD)
            .createdAt(UPDATED_CREATED_AT)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createUpdatedEntity();
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        updatedLesson.setTopic(topic);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedLesson.setCreatedBy(user);
        return updatedLesson;
    }

    @BeforeEach
    void initTest() {
        lesson = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLesson != null) {
            lessonRepository.delete(insertedLesson);
            insertedLesson = null;
        }
    }

    @Test
    @Transactional
    void createLesson() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);
        var returnedLessonDTO = om.readValue(
            restLessonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LessonDTO.class
        );

        // Validate the Lesson in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLesson = lessonMapper.toEntity(returnedLessonDTO);
        assertLessonUpdatableFieldsEquals(returnedLesson, getPersistedLesson(returnedLesson));

        insertedLesson = returnedLesson;
    }

    @Test
    @Transactional
    void createLessonWithExistingId() throws Exception {
        // Create the Lesson with an existing ID
        lesson.setId(1L);
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lesson.setTitle(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lesson.setLanguage(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lesson.setLevel(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkXpRewardIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lesson.setXpReward(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPassThresholdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lesson.setPassThreshold(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lesson.setCreatedAt(null);

        // Create the Lesson, which fails.
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        restLessonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLessons() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lesson.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].xpReward").value(hasItem(DEFAULT_XP_REWARD)))
            .andExpect(jsonPath("$.[*].passThreshold").value(hasItem(DEFAULT_PASS_THRESHOLD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonsWithEagerRelationshipsIsEnabled() throws Exception {
        when(lessonServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lessonServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lessonServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(lessonRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLesson() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get the lesson
        restLessonMockMvc
            .perform(get(ENTITY_API_URL_ID, lesson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lesson.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.xpReward").value(DEFAULT_XP_REWARD))
            .andExpect(jsonPath("$.passThreshold").value(DEFAULT_PASS_THRESHOLD))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getLessonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        Long id = lesson.getId();

        defaultLessonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLessonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLessonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLessonsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where title equals to
        defaultLessonFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllLessonsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where title in
        defaultLessonFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllLessonsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where title is not null
        defaultLessonFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where title contains
        defaultLessonFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllLessonsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where title does not contain
        defaultLessonFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllLessonsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where language equals to
        defaultLessonFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllLessonsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where language in
        defaultLessonFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllLessonsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where language is not null
        defaultLessonFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where level equals to
        defaultLessonFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllLessonsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where level in
        defaultLessonFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllLessonsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where level is not null
        defaultLessonFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByXpRewardIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where xpReward equals to
        defaultLessonFiltering("xpReward.equals=" + DEFAULT_XP_REWARD, "xpReward.equals=" + UPDATED_XP_REWARD);
    }

    @Test
    @Transactional
    void getAllLessonsByXpRewardIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where xpReward in
        defaultLessonFiltering("xpReward.in=" + DEFAULT_XP_REWARD + "," + UPDATED_XP_REWARD, "xpReward.in=" + UPDATED_XP_REWARD);
    }

    @Test
    @Transactional
    void getAllLessonsByXpRewardIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where xpReward is not null
        defaultLessonFiltering("xpReward.specified=true", "xpReward.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByXpRewardIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where xpReward is greater than or equal to
        defaultLessonFiltering("xpReward.greaterThanOrEqual=" + DEFAULT_XP_REWARD, "xpReward.greaterThanOrEqual=" + UPDATED_XP_REWARD);
    }

    @Test
    @Transactional
    void getAllLessonsByXpRewardIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where xpReward is less than or equal to
        defaultLessonFiltering("xpReward.lessThanOrEqual=" + DEFAULT_XP_REWARD, "xpReward.lessThanOrEqual=" + SMALLER_XP_REWARD);
    }

    @Test
    @Transactional
    void getAllLessonsByXpRewardIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where xpReward is less than
        defaultLessonFiltering("xpReward.lessThan=" + UPDATED_XP_REWARD, "xpReward.lessThan=" + DEFAULT_XP_REWARD);
    }

    @Test
    @Transactional
    void getAllLessonsByXpRewardIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where xpReward is greater than
        defaultLessonFiltering("xpReward.greaterThan=" + SMALLER_XP_REWARD, "xpReward.greaterThan=" + DEFAULT_XP_REWARD);
    }

    @Test
    @Transactional
    void getAllLessonsByPassThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where passThreshold equals to
        defaultLessonFiltering("passThreshold.equals=" + DEFAULT_PASS_THRESHOLD, "passThreshold.equals=" + UPDATED_PASS_THRESHOLD);
    }

    @Test
    @Transactional
    void getAllLessonsByPassThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where passThreshold in
        defaultLessonFiltering(
            "passThreshold.in=" + DEFAULT_PASS_THRESHOLD + "," + UPDATED_PASS_THRESHOLD,
            "passThreshold.in=" + UPDATED_PASS_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLessonsByPassThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where passThreshold is not null
        defaultLessonFiltering("passThreshold.specified=true", "passThreshold.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByPassThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where passThreshold is greater than or equal to
        defaultLessonFiltering(
            "passThreshold.greaterThanOrEqual=" + DEFAULT_PASS_THRESHOLD,
            "passThreshold.greaterThanOrEqual=" + (DEFAULT_PASS_THRESHOLD + 1)
        );
    }

    @Test
    @Transactional
    void getAllLessonsByPassThresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where passThreshold is less than or equal to
        defaultLessonFiltering(
            "passThreshold.lessThanOrEqual=" + DEFAULT_PASS_THRESHOLD,
            "passThreshold.lessThanOrEqual=" + SMALLER_PASS_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLessonsByPassThresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where passThreshold is less than
        defaultLessonFiltering(
            "passThreshold.lessThan=" + (DEFAULT_PASS_THRESHOLD + 1),
            "passThreshold.lessThan=" + DEFAULT_PASS_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLessonsByPassThresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where passThreshold is greater than
        defaultLessonFiltering(
            "passThreshold.greaterThan=" + SMALLER_PASS_THRESHOLD,
            "passThreshold.greaterThan=" + DEFAULT_PASS_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllLessonsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where createdAt equals to
        defaultLessonFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLessonsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where createdAt in
        defaultLessonFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllLessonsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where createdAt is not null
        defaultLessonFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where description equals to
        defaultLessonFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where description in
        defaultLessonFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLessonsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where description is not null
        defaultLessonFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllLessonsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where description contains
        defaultLessonFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        // Get all the lessonList where description does not contain
        defaultLessonFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLessonsByTopicIsEqualToSomething() throws Exception {
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            lessonRepository.saveAndFlush(lesson);
            topic = TopicResourceIT.createEntity();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        em.persist(topic);
        em.flush();
        lesson.setTopic(topic);
        lessonRepository.saveAndFlush(lesson);
        Long topicId = topic.getId();
        // Get all the lessonList where topic equals to topicId
        defaultLessonShouldBeFound("topicId.equals=" + topicId);

        // Get all the lessonList where topic equals to (topicId + 1)
        defaultLessonShouldNotBeFound("topicId.equals=" + (topicId + 1));
    }

    @Test
    @Transactional
    void getAllLessonsByCreatedByIsEqualToSomething() throws Exception {
        User createdBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            lessonRepository.saveAndFlush(lesson);
            createdBy = UserResourceIT.createEntity();
        } else {
            createdBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        lesson.setCreatedBy(createdBy);
        lessonRepository.saveAndFlush(lesson);
        Long createdById = createdBy.getId();
        // Get all the lessonList where createdBy equals to createdById
        defaultLessonShouldBeFound("createdById.equals=" + createdById);

        // Get all the lessonList where createdBy equals to (createdById + 1)
        defaultLessonShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    private void defaultLessonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLessonShouldBeFound(shouldBeFound);
        defaultLessonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLessonShouldBeFound(String filter) throws Exception {
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lesson.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].xpReward").value(hasItem(DEFAULT_XP_REWARD)))
            .andExpect(jsonPath("$.[*].passThreshold").value(hasItem(DEFAULT_PASS_THRESHOLD)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLessonShouldNotBeFound(String filter) throws Exception {
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLessonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLesson() throws Exception {
        // Get the lesson
        restLessonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLesson() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lesson
        Lesson updatedLesson = lessonRepository.findById(lesson.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLesson are not directly saved in db
        em.detach(updatedLesson);
        updatedLesson
            .title(UPDATED_TITLE)
            .language(UPDATED_LANGUAGE)
            .level(UPDATED_LEVEL)
            .xpReward(UPDATED_XP_REWARD)
            .passThreshold(UPDATED_PASS_THRESHOLD)
            .createdAt(UPDATED_CREATED_AT)
            .description(UPDATED_DESCRIPTION);
        LessonDTO lessonDTO = lessonMapper.toDto(updatedLesson);

        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLessonToMatchAllProperties(updatedLesson);
    }

    @Test
    @Transactional
    void putNonExistingLesson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lesson.setId(longCount.incrementAndGet());

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLesson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lesson.setId(longCount.incrementAndGet());

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lessonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLesson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lesson.setId(longCount.incrementAndGet());

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonWithPatch() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lesson using partial update
        Lesson partialUpdatedLesson = new Lesson();
        partialUpdatedLesson.setId(lesson.getId());

        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLessonUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLesson, lesson), getPersistedLesson(lesson));
    }

    @Test
    @Transactional
    void fullUpdateLessonWithPatch() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lesson using partial update
        Lesson partialUpdatedLesson = new Lesson();
        partialUpdatedLesson.setId(lesson.getId());

        partialUpdatedLesson
            .title(UPDATED_TITLE)
            .language(UPDATED_LANGUAGE)
            .level(UPDATED_LEVEL)
            .xpReward(UPDATED_XP_REWARD)
            .passThreshold(UPDATED_PASS_THRESHOLD)
            .createdAt(UPDATED_CREATED_AT)
            .description(UPDATED_DESCRIPTION);

        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLesson.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLesson))
            )
            .andExpect(status().isOk());

        // Validate the Lesson in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLessonUpdatableFieldsEquals(partialUpdatedLesson, getPersistedLesson(partialUpdatedLesson));
    }

    @Test
    @Transactional
    void patchNonExistingLesson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lesson.setId(longCount.incrementAndGet());

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lessonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLesson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lesson.setId(longCount.incrementAndGet());

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lessonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLesson() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lesson.setId(longCount.incrementAndGet());

        // Create the Lesson
        LessonDTO lessonDTO = lessonMapper.toDto(lesson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lessonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lesson in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLesson() throws Exception {
        // Initialize the database
        insertedLesson = lessonRepository.saveAndFlush(lesson);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lesson
        restLessonMockMvc
            .perform(delete(ENTITY_API_URL_ID, lesson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lessonRepository.count();
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

    protected Lesson getPersistedLesson(Lesson lesson) {
        return lessonRepository.findById(lesson.getId()).orElseThrow();
    }

    protected void assertPersistedLessonToMatchAllProperties(Lesson expectedLesson) {
        assertLessonAllPropertiesEquals(expectedLesson, getPersistedLesson(expectedLesson));
    }

    protected void assertPersistedLessonToMatchUpdatableProperties(Lesson expectedLesson) {
        assertLessonAllUpdatablePropertiesEquals(expectedLesson, getPersistedLesson(expectedLesson));
    }
}
