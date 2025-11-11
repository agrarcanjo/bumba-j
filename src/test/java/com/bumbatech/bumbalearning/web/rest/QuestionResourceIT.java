package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.QuestionAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.Topic;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import com.bumbatech.bumbalearning.domain.enumeration.QuestionType;
import com.bumbatech.bumbalearning.domain.enumeration.Skill;
import com.bumbatech.bumbalearning.repository.QuestionRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.QuestionService;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import com.bumbatech.bumbalearning.service.mapper.QuestionMapper;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    private static final QuestionType DEFAULT_TYPE = QuestionType.MULTIPLE_CHOICE;
    private static final QuestionType UPDATED_TYPE = QuestionType.FILL_BLANK;

    private static final Language DEFAULT_LANGUAGE = Language.ENGLISH;
    private static final Language UPDATED_LANGUAGE = Language.SPANISH;

    private static final Skill DEFAULT_SKILL = Skill.READING;
    private static final Skill UPDATED_SKILL = Skill.WRITING;

    private static final DifficultyLevel DEFAULT_LEVEL = DifficultyLevel.EASY;
    private static final DifficultyLevel UPDATED_LEVEL = DifficultyLevel.INTERMEDIATE;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PROMPT = "AAAAAAAAAA";
    private static final String UPDATED_PROMPT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_ASSETS = "AAAAAAAAAA";
    private static final String UPDATED_ASSETS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private QuestionRepository questionRepositoryMock;

    @Autowired
    private QuestionMapper questionMapper;

    @Mock
    private QuestionService questionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    private Question insertedQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .type(DEFAULT_TYPE)
            .language(DEFAULT_LANGUAGE)
            .skill(DEFAULT_SKILL)
            .level(DEFAULT_LEVEL)
            .createdAt(DEFAULT_CREATED_AT)
            .prompt(DEFAULT_PROMPT)
            .content(DEFAULT_CONTENT)
            .assets(DEFAULT_ASSETS);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createEntity();
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        question.setTopic(topic);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        question.setCreatedBy(user);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question updatedQuestion = new Question()
            .type(UPDATED_TYPE)
            .language(UPDATED_LANGUAGE)
            .skill(UPDATED_SKILL)
            .level(UPDATED_LEVEL)
            .createdAt(UPDATED_CREATED_AT)
            .prompt(UPDATED_PROMPT)
            .content(UPDATED_CONTENT)
            .assets(UPDATED_ASSETS);
        // Add required entity
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            topic = TopicResourceIT.createUpdatedEntity();
            em.persist(topic);
            em.flush();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        updatedQuestion.setTopic(topic);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedQuestion.setCreatedBy(user);
        return updatedQuestion;
    }

    @BeforeEach
    void initTest() {
        question = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuestion != null) {
            questionRepository.delete(insertedQuestion);
            insertedQuestion = null;
        }
    }

    @Test
    @Transactional
    void createQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);
        var returnedQuestionDTO = om.readValue(
            restQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuestionDTO.class
        );

        // Validate the Question in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuestion = questionMapper.toEntity(returnedQuestionDTO);
        assertQuestionUpdatableFieldsEquals(returnedQuestion, getPersistedQuestion(returnedQuestion));

        insertedQuestion = returnedQuestion;
    }

    @Test
    @Transactional
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId(1L);
        QuestionDTO questionDTO = questionMapper.toDto(question);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setType(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setLanguage(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSkillIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setSkill(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setLevel(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setCreatedAt(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPromptIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        question.setPrompt(null);

        // Create the Question, which fails.
        QuestionDTO questionDTO = questionMapper.toDto(question);

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].skill").value(hasItem(DEFAULT_SKILL.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].prompt").value(hasItem(DEFAULT_PROMPT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].assets").value(hasItem(DEFAULT_ASSETS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(questionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.skill").value(DEFAULT_SKILL.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.prompt").value(DEFAULT_PROMPT))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.assets").value(DEFAULT_ASSETS));
    }

    @Test
    @Transactional
    void getQuestionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        Long id = question.getId();

        defaultQuestionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQuestionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQuestionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where type equals to
        defaultQuestionFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where type in
        defaultQuestionFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where type is not null
        defaultQuestionFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where language equals to
        defaultQuestionFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where language in
        defaultQuestionFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllQuestionsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where language is not null
        defaultQuestionFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsBySkillIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where skill equals to
        defaultQuestionFiltering("skill.equals=" + DEFAULT_SKILL, "skill.equals=" + UPDATED_SKILL);
    }

    @Test
    @Transactional
    void getAllQuestionsBySkillIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where skill in
        defaultQuestionFiltering("skill.in=" + DEFAULT_SKILL + "," + UPDATED_SKILL, "skill.in=" + UPDATED_SKILL);
    }

    @Test
    @Transactional
    void getAllQuestionsBySkillIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where skill is not null
        defaultQuestionFiltering("skill.specified=true", "skill.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where level equals to
        defaultQuestionFiltering("level.equals=" + DEFAULT_LEVEL, "level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllQuestionsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where level in
        defaultQuestionFiltering("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL, "level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllQuestionsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where level is not null
        defaultQuestionFiltering("level.specified=true", "level.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createdAt equals to
        defaultQuestionFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createdAt in
        defaultQuestionFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllQuestionsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where createdAt is not null
        defaultQuestionFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByPromptIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where prompt equals to
        defaultQuestionFiltering("prompt.equals=" + DEFAULT_PROMPT, "prompt.equals=" + UPDATED_PROMPT);
    }

    @Test
    @Transactional
    void getAllQuestionsByPromptIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where prompt in
        defaultQuestionFiltering("prompt.in=" + DEFAULT_PROMPT + "," + UPDATED_PROMPT, "prompt.in=" + UPDATED_PROMPT);
    }

    @Test
    @Transactional
    void getAllQuestionsByPromptIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where prompt is not null
        defaultQuestionFiltering("prompt.specified=true", "prompt.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionsByPromptContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where prompt contains
        defaultQuestionFiltering("prompt.contains=" + DEFAULT_PROMPT, "prompt.contains=" + UPDATED_PROMPT);
    }

    @Test
    @Transactional
    void getAllQuestionsByPromptNotContainsSomething() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        // Get all the questionList where prompt does not contain
        defaultQuestionFiltering("prompt.doesNotContain=" + UPDATED_PROMPT, "prompt.doesNotContain=" + DEFAULT_PROMPT);
    }

    @Test
    @Transactional
    void getAllQuestionsByTopicIsEqualToSomething() throws Exception {
        Topic topic;
        if (TestUtil.findAll(em, Topic.class).isEmpty()) {
            questionRepository.saveAndFlush(question);
            topic = TopicResourceIT.createEntity();
        } else {
            topic = TestUtil.findAll(em, Topic.class).get(0);
        }
        em.persist(topic);
        em.flush();
        question.setTopic(topic);
        questionRepository.saveAndFlush(question);
        Long topicId = topic.getId();
        // Get all the questionList where topic equals to topicId
        defaultQuestionShouldBeFound("topicId.equals=" + topicId);

        // Get all the questionList where topic equals to (topicId + 1)
        defaultQuestionShouldNotBeFound("topicId.equals=" + (topicId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionsByCreatedByIsEqualToSomething() throws Exception {
        User createdBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            questionRepository.saveAndFlush(question);
            createdBy = UserResourceIT.createEntity();
        } else {
            createdBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        question.setCreatedBy(createdBy);
        questionRepository.saveAndFlush(question);
        Long createdById = createdBy.getId();
        // Get all the questionList where createdBy equals to createdById
        defaultQuestionShouldBeFound("createdById.equals=" + createdById);

        // Get all the questionList where createdBy equals to (createdById + 1)
        defaultQuestionShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    private void defaultQuestionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuestionShouldBeFound(shouldBeFound);
        defaultQuestionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].skill").value(hasItem(DEFAULT_SKILL.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].prompt").value(hasItem(DEFAULT_PROMPT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].assets").value(hasItem(DEFAULT_ASSETS)));

        // Check, that the count call also returns 1
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .type(UPDATED_TYPE)
            .language(UPDATED_LANGUAGE)
            .skill(UPDATED_SKILL)
            .level(UPDATED_LEVEL)
            .createdAt(UPDATED_CREATED_AT)
            .prompt(UPDATED_PROMPT)
            .content(UPDATED_CONTENT)
            .assets(UPDATED_ASSETS);
        QuestionDTO questionDTO = questionMapper.toDto(updatedQuestion);

        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuestionToMatchAllProperties(updatedQuestion);
    }

    @Test
    @Transactional
    void putNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.skill(UPDATED_SKILL).level(UPDATED_LEVEL).content(UPDATED_CONTENT);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuestion, question), getPersistedQuestion(question));
    }

    @Test
    @Transactional
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .type(UPDATED_TYPE)
            .language(UPDATED_LANGUAGE)
            .skill(UPDATED_SKILL)
            .level(UPDATED_LEVEL)
            .createdAt(UPDATED_CREATED_AT)
            .prompt(UPDATED_PROMPT)
            .content(UPDATED_CONTENT)
            .assets(UPDATED_ASSETS);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuestionUpdatableFieldsEquals(partialUpdatedQuestion, getPersistedQuestion(partialUpdatedQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(questionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        question.setId(longCount.incrementAndGet());

        // Create the Question
        QuestionDTO questionDTO = questionMapper.toDto(question);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(questionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestion() throws Exception {
        // Initialize the database
        insertedQuestion = questionRepository.saveAndFlush(question);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the question
        restQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, question.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return questionRepository.count();
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

    protected Question getPersistedQuestion(Question question) {
        return questionRepository.findById(question.getId()).orElseThrow();
    }

    protected void assertPersistedQuestionToMatchAllProperties(Question expectedQuestion) {
        assertQuestionAllPropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }

    protected void assertPersistedQuestionToMatchUpdatableProperties(Question expectedQuestion) {
        assertQuestionAllUpdatablePropertiesEquals(expectedQuestion, getPersistedQuestion(expectedQuestion));
    }
}
