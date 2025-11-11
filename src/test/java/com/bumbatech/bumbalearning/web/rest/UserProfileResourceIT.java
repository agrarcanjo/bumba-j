package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.UserProfileAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.dto.UserProfileDTO;
import com.bumbatech.bumbalearning.service.mapper.UserProfileMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserProfileResourceIT {

    private static final String DEFAULT_MUNICIPALITY_CODE = "AAAAAAA";
    private static final String UPDATED_MUNICIPALITY_CODE = "BBBBBBB";

    private static final UserLevel DEFAULT_CURRENT_LEVEL = UserLevel.BEGINNER;
    private static final UserLevel UPDATED_CURRENT_LEVEL = UserLevel.INTERMEDIATE;

    private static final Integer DEFAULT_TOTAL_XP = 0;
    private static final Integer UPDATED_TOTAL_XP = 1;
    private static final Integer SMALLER_TOTAL_XP = 0 - 1;

    private static final Integer DEFAULT_CURRENT_STREAK = 0;
    private static final Integer UPDATED_CURRENT_STREAK = 1;
    private static final Integer SMALLER_CURRENT_STREAK = 0 - 1;

    private static final Integer DEFAULT_DAILY_GOAL_XP = 10;
    private static final Integer UPDATED_DAILY_GOAL_XP = 11;
    private static final Integer SMALLER_DAILY_GOAL_XP = 10 - 1;

    private static final LocalDate DEFAULT_LAST_ACTIVITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_ACTIVITY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_ACTIVITY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    private UserProfile insertedUserProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .municipalityCode(DEFAULT_MUNICIPALITY_CODE)
            .currentLevel(DEFAULT_CURRENT_LEVEL)
            .totalXp(DEFAULT_TOTAL_XP)
            .currentStreak(DEFAULT_CURRENT_STREAK)
            .dailyGoalXp(DEFAULT_DAILY_GOAL_XP)
            .lastActivityDate(DEFAULT_LAST_ACTIVITY_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        userProfile.setUser(user);
        return userProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity(EntityManager em) {
        UserProfile updatedUserProfile = new UserProfile()
            .municipalityCode(UPDATED_MUNICIPALITY_CODE)
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .totalXp(UPDATED_TOTAL_XP)
            .currentStreak(UPDATED_CURRENT_STREAK)
            .dailyGoalXp(UPDATED_DAILY_GOAL_XP)
            .lastActivityDate(UPDATED_LAST_ACTIVITY_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedUserProfile.setUser(user);
        return updatedUserProfile;
    }

    @BeforeEach
    void initTest() {
        userProfile = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUserProfile != null) {
            userProfileRepository.delete(insertedUserProfile);
            insertedUserProfile = null;
        }
    }

    @Test
    @Transactional
    void createUserProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        var returnedUserProfileDTO = om.readValue(
            restUserProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserProfileDTO.class
        );

        // Validate the UserProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserProfile = userProfileMapper.toEntity(returnedUserProfileDTO);
        assertUserProfileUpdatableFieldsEquals(returnedUserProfile, getPersistedUserProfile(returnedUserProfile));

        insertedUserProfile = returnedUserProfile;
    }

    @Test
    @Transactional
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMunicipalityCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setMunicipalityCode(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setCurrentLevel(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalXpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setTotalXp(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentStreakIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setCurrentStreak(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDailyGoalXpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userProfile.setDailyGoalXp(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserProfiles() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].municipalityCode").value(hasItem(DEFAULT_MUNICIPALITY_CODE)))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].totalXp").value(hasItem(DEFAULT_TOTAL_XP)))
            .andExpect(jsonPath("$.[*].currentStreak").value(hasItem(DEFAULT_CURRENT_STREAK)))
            .andExpect(jsonPath("$.[*].dailyGoalXp").value(hasItem(DEFAULT_DAILY_GOAL_XP)))
            .andExpect(jsonPath("$.[*].lastActivityDate").value(hasItem(DEFAULT_LAST_ACTIVITY_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.municipalityCode").value(DEFAULT_MUNICIPALITY_CODE))
            .andExpect(jsonPath("$.currentLevel").value(DEFAULT_CURRENT_LEVEL.toString()))
            .andExpect(jsonPath("$.totalXp").value(DEFAULT_TOTAL_XP))
            .andExpect(jsonPath("$.currentStreak").value(DEFAULT_CURRENT_STREAK))
            .andExpect(jsonPath("$.dailyGoalXp").value(DEFAULT_DAILY_GOAL_XP))
            .andExpect(jsonPath("$.lastActivityDate").value(DEFAULT_LAST_ACTIVITY_DATE.toString()));
    }

    @Test
    @Transactional
    void getUserProfilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        Long id = userProfile.getId();

        defaultUserProfileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserProfilesByMunicipalityCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where municipalityCode equals to
        defaultUserProfileFiltering(
            "municipalityCode.equals=" + DEFAULT_MUNICIPALITY_CODE,
            "municipalityCode.equals=" + UPDATED_MUNICIPALITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByMunicipalityCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where municipalityCode in
        defaultUserProfileFiltering(
            "municipalityCode.in=" + DEFAULT_MUNICIPALITY_CODE + "," + UPDATED_MUNICIPALITY_CODE,
            "municipalityCode.in=" + UPDATED_MUNICIPALITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByMunicipalityCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where municipalityCode is not null
        defaultUserProfileFiltering("municipalityCode.specified=true", "municipalityCode.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByMunicipalityCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where municipalityCode contains
        defaultUserProfileFiltering(
            "municipalityCode.contains=" + DEFAULT_MUNICIPALITY_CODE,
            "municipalityCode.contains=" + UPDATED_MUNICIPALITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByMunicipalityCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where municipalityCode does not contain
        defaultUserProfileFiltering(
            "municipalityCode.doesNotContain=" + UPDATED_MUNICIPALITY_CODE,
            "municipalityCode.doesNotContain=" + DEFAULT_MUNICIPALITY_CODE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentLevel equals to
        defaultUserProfileFiltering("currentLevel.equals=" + DEFAULT_CURRENT_LEVEL, "currentLevel.equals=" + UPDATED_CURRENT_LEVEL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentLevel in
        defaultUserProfileFiltering(
            "currentLevel.in=" + DEFAULT_CURRENT_LEVEL + "," + UPDATED_CURRENT_LEVEL,
            "currentLevel.in=" + UPDATED_CURRENT_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentLevel is not null
        defaultUserProfileFiltering("currentLevel.specified=true", "currentLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByTotalXpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where totalXp equals to
        defaultUserProfileFiltering("totalXp.equals=" + DEFAULT_TOTAL_XP, "totalXp.equals=" + UPDATED_TOTAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByTotalXpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where totalXp in
        defaultUserProfileFiltering("totalXp.in=" + DEFAULT_TOTAL_XP + "," + UPDATED_TOTAL_XP, "totalXp.in=" + UPDATED_TOTAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByTotalXpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where totalXp is not null
        defaultUserProfileFiltering("totalXp.specified=true", "totalXp.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByTotalXpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where totalXp is greater than or equal to
        defaultUserProfileFiltering("totalXp.greaterThanOrEqual=" + DEFAULT_TOTAL_XP, "totalXp.greaterThanOrEqual=" + UPDATED_TOTAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByTotalXpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where totalXp is less than or equal to
        defaultUserProfileFiltering("totalXp.lessThanOrEqual=" + DEFAULT_TOTAL_XP, "totalXp.lessThanOrEqual=" + SMALLER_TOTAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByTotalXpIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where totalXp is less than
        defaultUserProfileFiltering("totalXp.lessThan=" + UPDATED_TOTAL_XP, "totalXp.lessThan=" + DEFAULT_TOTAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByTotalXpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where totalXp is greater than
        defaultUserProfileFiltering("totalXp.greaterThan=" + SMALLER_TOTAL_XP, "totalXp.greaterThan=" + DEFAULT_TOTAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentStreakIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentStreak equals to
        defaultUserProfileFiltering("currentStreak.equals=" + DEFAULT_CURRENT_STREAK, "currentStreak.equals=" + UPDATED_CURRENT_STREAK);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentStreakIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentStreak in
        defaultUserProfileFiltering(
            "currentStreak.in=" + DEFAULT_CURRENT_STREAK + "," + UPDATED_CURRENT_STREAK,
            "currentStreak.in=" + UPDATED_CURRENT_STREAK
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentStreakIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentStreak is not null
        defaultUserProfileFiltering("currentStreak.specified=true", "currentStreak.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentStreakIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentStreak is greater than or equal to
        defaultUserProfileFiltering(
            "currentStreak.greaterThanOrEqual=" + DEFAULT_CURRENT_STREAK,
            "currentStreak.greaterThanOrEqual=" + UPDATED_CURRENT_STREAK
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentStreakIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentStreak is less than or equal to
        defaultUserProfileFiltering(
            "currentStreak.lessThanOrEqual=" + DEFAULT_CURRENT_STREAK,
            "currentStreak.lessThanOrEqual=" + SMALLER_CURRENT_STREAK
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentStreakIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentStreak is less than
        defaultUserProfileFiltering("currentStreak.lessThan=" + UPDATED_CURRENT_STREAK, "currentStreak.lessThan=" + DEFAULT_CURRENT_STREAK);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCurrentStreakIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where currentStreak is greater than
        defaultUserProfileFiltering(
            "currentStreak.greaterThan=" + SMALLER_CURRENT_STREAK,
            "currentStreak.greaterThan=" + DEFAULT_CURRENT_STREAK
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDailyGoalXpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dailyGoalXp equals to
        defaultUserProfileFiltering("dailyGoalXp.equals=" + DEFAULT_DAILY_GOAL_XP, "dailyGoalXp.equals=" + UPDATED_DAILY_GOAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByDailyGoalXpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dailyGoalXp in
        defaultUserProfileFiltering(
            "dailyGoalXp.in=" + DEFAULT_DAILY_GOAL_XP + "," + UPDATED_DAILY_GOAL_XP,
            "dailyGoalXp.in=" + UPDATED_DAILY_GOAL_XP
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDailyGoalXpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dailyGoalXp is not null
        defaultUserProfileFiltering("dailyGoalXp.specified=true", "dailyGoalXp.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByDailyGoalXpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dailyGoalXp is greater than or equal to
        defaultUserProfileFiltering(
            "dailyGoalXp.greaterThanOrEqual=" + DEFAULT_DAILY_GOAL_XP,
            "dailyGoalXp.greaterThanOrEqual=" + UPDATED_DAILY_GOAL_XP
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDailyGoalXpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dailyGoalXp is less than or equal to
        defaultUserProfileFiltering(
            "dailyGoalXp.lessThanOrEqual=" + DEFAULT_DAILY_GOAL_XP,
            "dailyGoalXp.lessThanOrEqual=" + SMALLER_DAILY_GOAL_XP
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByDailyGoalXpIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dailyGoalXp is less than
        defaultUserProfileFiltering("dailyGoalXp.lessThan=" + UPDATED_DAILY_GOAL_XP, "dailyGoalXp.lessThan=" + DEFAULT_DAILY_GOAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByDailyGoalXpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where dailyGoalXp is greater than
        defaultUserProfileFiltering("dailyGoalXp.greaterThan=" + SMALLER_DAILY_GOAL_XP, "dailyGoalXp.greaterThan=" + DEFAULT_DAILY_GOAL_XP);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastActivityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastActivityDate equals to
        defaultUserProfileFiltering(
            "lastActivityDate.equals=" + DEFAULT_LAST_ACTIVITY_DATE,
            "lastActivityDate.equals=" + UPDATED_LAST_ACTIVITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastActivityDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastActivityDate in
        defaultUserProfileFiltering(
            "lastActivityDate.in=" + DEFAULT_LAST_ACTIVITY_DATE + "," + UPDATED_LAST_ACTIVITY_DATE,
            "lastActivityDate.in=" + UPDATED_LAST_ACTIVITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastActivityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastActivityDate is not null
        defaultUserProfileFiltering("lastActivityDate.specified=true", "lastActivityDate.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastActivityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastActivityDate is greater than or equal to
        defaultUserProfileFiltering(
            "lastActivityDate.greaterThanOrEqual=" + DEFAULT_LAST_ACTIVITY_DATE,
            "lastActivityDate.greaterThanOrEqual=" + UPDATED_LAST_ACTIVITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastActivityDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastActivityDate is less than or equal to
        defaultUserProfileFiltering(
            "lastActivityDate.lessThanOrEqual=" + DEFAULT_LAST_ACTIVITY_DATE,
            "lastActivityDate.lessThanOrEqual=" + SMALLER_LAST_ACTIVITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastActivityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastActivityDate is less than
        defaultUserProfileFiltering(
            "lastActivityDate.lessThan=" + UPDATED_LAST_ACTIVITY_DATE,
            "lastActivityDate.lessThan=" + DEFAULT_LAST_ACTIVITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastActivityDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastActivityDate is greater than
        defaultUserProfileFiltering(
            "lastActivityDate.greaterThan=" + SMALLER_LAST_ACTIVITY_DATE,
            "lastActivityDate.greaterThan=" + DEFAULT_LAST_ACTIVITY_DATE
        );
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userProfile.getUser();
        userProfileRepository.saveAndFlush(userProfile);
        Long userId = user.getId();
        // Get all the userProfileList where user equals to userId
        defaultUserProfileShouldBeFound("userId.equals=" + userId);

        // Get all the userProfileList where user equals to (userId + 1)
        defaultUserProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultUserProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserProfileShouldBeFound(shouldBeFound);
        defaultUserProfileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].municipalityCode").value(hasItem(DEFAULT_MUNICIPALITY_CODE)))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].totalXp").value(hasItem(DEFAULT_TOTAL_XP)))
            .andExpect(jsonPath("$.[*].currentStreak").value(hasItem(DEFAULT_CURRENT_STREAK)))
            .andExpect(jsonPath("$.[*].dailyGoalXp").value(hasItem(DEFAULT_DAILY_GOAL_XP)))
            .andExpect(jsonPath("$.[*].lastActivityDate").value(hasItem(DEFAULT_LAST_ACTIVITY_DATE.toString())));

        // Check, that the count call also returns 1
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .municipalityCode(UPDATED_MUNICIPALITY_CODE)
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .totalXp(UPDATED_TOTAL_XP)
            .currentStreak(UPDATED_CURRENT_STREAK)
            .dailyGoalXp(UPDATED_DAILY_GOAL_XP)
            .lastActivityDate(UPDATED_LAST_ACTIVITY_DATE);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserProfileToMatchAllProperties(updatedUserProfile);
    }

    @Test
    @Transactional
    void putNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .totalXp(UPDATED_TOTAL_XP)
            .currentStreak(UPDATED_CURRENT_STREAK)
            .lastActivityDate(UPDATED_LAST_ACTIVITY_DATE);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserProfile, userProfile),
            getPersistedUserProfile(userProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .municipalityCode(UPDATED_MUNICIPALITY_CODE)
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .totalXp(UPDATED_TOTAL_XP)
            .currentStreak(UPDATED_CURRENT_STREAK)
            .dailyGoalXp(UPDATED_DAILY_GOAL_XP)
            .lastActivityDate(UPDATED_LAST_ACTIVITY_DATE);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserProfileUpdatableFieldsEquals(partialUpdatedUserProfile, getPersistedUserProfile(partialUpdatedUserProfile));
    }

    @Test
    @Transactional
    void patchNonExistingUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserProfile() throws Exception {
        // Initialize the database
        insertedUserProfile = userProfileRepository.saveAndFlush(userProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userProfile
        restUserProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, userProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userProfileRepository.count();
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

    protected UserProfile getPersistedUserProfile(UserProfile userProfile) {
        return userProfileRepository.findById(userProfile.getId()).orElseThrow();
    }

    protected void assertPersistedUserProfileToMatchAllProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllPropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }

    protected void assertPersistedUserProfileToMatchUpdatableProperties(UserProfile expectedUserProfile) {
        assertUserProfileAllUpdatablePropertiesEquals(expectedUserProfile, getPersistedUserProfile(expectedUserProfile));
    }
}
