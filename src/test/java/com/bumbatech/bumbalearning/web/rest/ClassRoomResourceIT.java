package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.ClassRoomAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import com.bumbatech.bumbalearning.repository.ClassRoomRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.mapper.ClassRoomMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ClassRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassRoomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.ENGLISH;
    private static final Language UPDATED_LANGUAGE = Language.SPANISH;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/class-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRoomMapper classRoomMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassRoomMockMvc;

    private ClassRoom classRoom;

    private ClassRoom insertedClassRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassRoom createEntity(EntityManager em) {
        ClassRoom classRoom = new ClassRoom()
            .name(DEFAULT_NAME)
            .language(DEFAULT_LANGUAGE)
            .createdAt(DEFAULT_CREATED_AT)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        classRoom.setTeacher(user);
        return classRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassRoom createUpdatedEntity(EntityManager em) {
        ClassRoom updatedClassRoom = new ClassRoom()
            .name(UPDATED_NAME)
            .language(UPDATED_LANGUAGE)
            .createdAt(UPDATED_CREATED_AT)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedClassRoom.setTeacher(user);
        return updatedClassRoom;
    }

    @BeforeEach
    void initTest() {
        classRoom = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedClassRoom != null) {
            classRoomRepository.delete(insertedClassRoom);
            insertedClassRoom = null;
        }
    }

    @Test
    @Transactional
    void createClassRoom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);
        var returnedClassRoomDTO = om.readValue(
            restClassRoomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classRoomDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClassRoomDTO.class
        );

        // Validate the ClassRoom in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClassRoom = classRoomMapper.toEntity(returnedClassRoomDTO);
        assertClassRoomUpdatableFieldsEquals(returnedClassRoom, getPersistedClassRoom(returnedClassRoom));

        insertedClassRoom = returnedClassRoom;
    }

    @Test
    @Transactional
    void createClassRoomWithExistingId() throws Exception {
        // Create the ClassRoom with an existing ID
        classRoom.setId(1L);
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classRoomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classRoom.setName(null);

        // Create the ClassRoom, which fails.
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        restClassRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classRoomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classRoom.setLanguage(null);

        // Create the ClassRoom, which fails.
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        restClassRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classRoomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classRoom.setCreatedAt(null);

        // Create the ClassRoom, which fails.
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        restClassRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classRoomDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassRooms() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList
        restClassRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getClassRoom() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get the classRoom
        restClassRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, classRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classRoom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getClassRoomsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        Long id = classRoom.getId();

        defaultClassRoomFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClassRoomFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClassRoomFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassRoomsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where name equals to
        defaultClassRoomFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassRoomsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where name in
        defaultClassRoomFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassRoomsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where name is not null
        defaultClassRoomFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllClassRoomsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where name contains
        defaultClassRoomFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClassRoomsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where name does not contain
        defaultClassRoomFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllClassRoomsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where language equals to
        defaultClassRoomFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllClassRoomsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where language in
        defaultClassRoomFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllClassRoomsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where language is not null
        defaultClassRoomFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllClassRoomsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where createdAt equals to
        defaultClassRoomFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClassRoomsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where createdAt in
        defaultClassRoomFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllClassRoomsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where createdAt is not null
        defaultClassRoomFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllClassRoomsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where description equals to
        defaultClassRoomFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassRoomsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where description in
        defaultClassRoomFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllClassRoomsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where description is not null
        defaultClassRoomFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllClassRoomsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where description contains
        defaultClassRoomFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassRoomsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRoomList where description does not contain
        defaultClassRoomFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClassRoomsByTeacherIsEqualToSomething() throws Exception {
        User teacher;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            classRoomRepository.saveAndFlush(classRoom);
            teacher = UserResourceIT.createEntity();
        } else {
            teacher = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(teacher);
        em.flush();
        classRoom.setTeacher(teacher);
        classRoomRepository.saveAndFlush(classRoom);
        Long teacherId = teacher.getId();
        // Get all the classRoomList where teacher equals to teacherId
        defaultClassRoomShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the classRoomList where teacher equals to (teacherId + 1)
        defaultClassRoomShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }

    private void defaultClassRoomFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClassRoomShouldBeFound(shouldBeFound);
        defaultClassRoomShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassRoomShouldBeFound(String filter) throws Exception {
        restClassRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restClassRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassRoomShouldNotBeFound(String filter) throws Exception {
        restClassRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassRoomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassRoom() throws Exception {
        // Get the classRoom
        restClassRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClassRoom() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classRoom
        ClassRoom updatedClassRoom = classRoomRepository.findById(classRoom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClassRoom are not directly saved in db
        em.detach(updatedClassRoom);
        updatedClassRoom.name(UPDATED_NAME).language(UPDATED_LANGUAGE).createdAt(UPDATED_CREATED_AT).description(UPDATED_DESCRIPTION);
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(updatedClassRoom);

        restClassRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classRoomDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClassRoomToMatchAllProperties(updatedClassRoom);
    }

    @Test
    @Transactional
    void putNonExistingClassRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classRoom.setId(longCount.incrementAndGet());

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classRoomDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classRoom.setId(longCount.incrementAndGet());

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classRoom.setId(longCount.incrementAndGet());

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classRoomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassRoomWithPatch() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classRoom using partial update
        ClassRoom partialUpdatedClassRoom = new ClassRoom();
        partialUpdatedClassRoom.setId(classRoom.getId());

        partialUpdatedClassRoom.language(UPDATED_LANGUAGE);

        restClassRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassRoom))
            )
            .andExpect(status().isOk());

        // Validate the ClassRoom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassRoomUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClassRoom, classRoom),
            getPersistedClassRoom(classRoom)
        );
    }

    @Test
    @Transactional
    void fullUpdateClassRoomWithPatch() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classRoom using partial update
        ClassRoom partialUpdatedClassRoom = new ClassRoom();
        partialUpdatedClassRoom.setId(classRoom.getId());

        partialUpdatedClassRoom
            .name(UPDATED_NAME)
            .language(UPDATED_LANGUAGE)
            .createdAt(UPDATED_CREATED_AT)
            .description(UPDATED_DESCRIPTION);

        restClassRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassRoom))
            )
            .andExpect(status().isOk());

        // Validate the ClassRoom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassRoomUpdatableFieldsEquals(partialUpdatedClassRoom, getPersistedClassRoom(partialUpdatedClassRoom));
    }

    @Test
    @Transactional
    void patchNonExistingClassRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classRoom.setId(longCount.incrementAndGet());

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classRoomDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classRoom.setId(longCount.incrementAndGet());

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classRoomDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classRoom.setId(longCount.incrementAndGet());

        // Create the ClassRoom
        ClassRoomDTO classRoomDTO = classRoomMapper.toDto(classRoom);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(classRoomDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassRoom() throws Exception {
        // Initialize the database
        insertedClassRoom = classRoomRepository.saveAndFlush(classRoom);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the classRoom
        restClassRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, classRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return classRoomRepository.count();
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

    protected ClassRoom getPersistedClassRoom(ClassRoom classRoom) {
        return classRoomRepository.findById(classRoom.getId()).orElseThrow();
    }

    protected void assertPersistedClassRoomToMatchAllProperties(ClassRoom expectedClassRoom) {
        assertClassRoomAllPropertiesEquals(expectedClassRoom, getPersistedClassRoom(expectedClassRoom));
    }

    protected void assertPersistedClassRoomToMatchUpdatableProperties(ClassRoom expectedClassRoom) {
        assertClassRoomAllUpdatablePropertiesEquals(expectedClassRoom, getPersistedClassRoom(expectedClassRoom));
    }
}
