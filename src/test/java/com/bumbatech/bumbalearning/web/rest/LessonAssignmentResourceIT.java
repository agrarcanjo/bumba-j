package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.LessonAssignmentAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.LessonAssignment;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.LessonAssignmentRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.LessonAssignmentService;
import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import com.bumbatech.bumbalearning.service.mapper.LessonAssignmentMapper;
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
 * Integration tests for the {@link LessonAssignmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LessonAssignmentResourceIT {

    private static final Instant DEFAULT_ASSIGNED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ASSIGNED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/lesson-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LessonAssignmentRepository lessonAssignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private LessonAssignmentRepository lessonAssignmentRepositoryMock;

    @Autowired
    private LessonAssignmentMapper lessonAssignmentMapper;

    @Mock
    private LessonAssignmentService lessonAssignmentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLessonAssignmentMockMvc;

    private LessonAssignment lessonAssignment;

    private LessonAssignment insertedLessonAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonAssignment createEntity(EntityManager em) {
        LessonAssignment lessonAssignment = new LessonAssignment().assignedAt(DEFAULT_ASSIGNED_AT).dueDate(DEFAULT_DUE_DATE);
        // Add required entity
        ClassRoom classRoom;
        if (TestUtil.findAll(em, ClassRoom.class).isEmpty()) {
            classRoom = ClassRoomResourceIT.createEntity(em);
            em.persist(classRoom);
            em.flush();
        } else {
            classRoom = TestUtil.findAll(em, ClassRoom.class).get(0);
        }
        lessonAssignment.setClassRoom(classRoom);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        lessonAssignment.setLesson(lesson);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        lessonAssignment.setAssignedBy(user);
        return lessonAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LessonAssignment createUpdatedEntity(EntityManager em) {
        LessonAssignment updatedLessonAssignment = new LessonAssignment().assignedAt(UPDATED_ASSIGNED_AT).dueDate(UPDATED_DUE_DATE);
        // Add required entity
        ClassRoom classRoom;
        if (TestUtil.findAll(em, ClassRoom.class).isEmpty()) {
            classRoom = ClassRoomResourceIT.createUpdatedEntity(em);
            em.persist(classRoom);
            em.flush();
        } else {
            classRoom = TestUtil.findAll(em, ClassRoom.class).get(0);
        }
        updatedLessonAssignment.setClassRoom(classRoom);
        // Add required entity
        Lesson lesson;
        if (TestUtil.findAll(em, Lesson.class).isEmpty()) {
            lesson = LessonResourceIT.createUpdatedEntity(em);
            em.persist(lesson);
            em.flush();
        } else {
            lesson = TestUtil.findAll(em, Lesson.class).get(0);
        }
        updatedLessonAssignment.setLesson(lesson);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedLessonAssignment.setAssignedBy(user);
        return updatedLessonAssignment;
    }

    @BeforeEach
    void initTest() {
        lessonAssignment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLessonAssignment != null) {
            lessonAssignmentRepository.delete(insertedLessonAssignment);
            insertedLessonAssignment = null;
        }
    }

    @Test
    @Transactional
    void createLessonAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LessonAssignment
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);
        var returnedLessonAssignmentDTO = om.readValue(
            restLessonAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonAssignmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LessonAssignmentDTO.class
        );

        // Validate the LessonAssignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLessonAssignment = lessonAssignmentMapper.toEntity(returnedLessonAssignmentDTO);
        assertLessonAssignmentUpdatableFieldsEquals(returnedLessonAssignment, getPersistedLessonAssignment(returnedLessonAssignment));

        insertedLessonAssignment = returnedLessonAssignment;
    }

    @Test
    @Transactional
    void createLessonAssignmentWithExistingId() throws Exception {
        // Create the LessonAssignment with an existing ID
        lessonAssignment.setId(1L);
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLessonAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonAssignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAssignedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lessonAssignment.setAssignedAt(null);

        // Create the LessonAssignment, which fails.
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        restLessonAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDueDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lessonAssignment.setDueDate(null);

        // Create the LessonAssignment, which fails.
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        restLessonAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonAssignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLessonAssignments() throws Exception {
        // Initialize the database
        insertedLessonAssignment = lessonAssignmentRepository.saveAndFlush(lessonAssignment);

        // Get all the lessonAssignmentList
        restLessonAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lessonAssignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].assignedAt").value(hasItem(DEFAULT_ASSIGNED_AT.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonAssignmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(lessonAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lessonAssignmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLessonAssignmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lessonAssignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLessonAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(lessonAssignmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLessonAssignment() throws Exception {
        // Initialize the database
        insertedLessonAssignment = lessonAssignmentRepository.saveAndFlush(lessonAssignment);

        // Get the lessonAssignment
        restLessonAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, lessonAssignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lessonAssignment.getId().intValue()))
            .andExpect(jsonPath("$.assignedAt").value(DEFAULT_ASSIGNED_AT.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLessonAssignment() throws Exception {
        // Get the lessonAssignment
        restLessonAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLessonAssignment() throws Exception {
        // Initialize the database
        insertedLessonAssignment = lessonAssignmentRepository.saveAndFlush(lessonAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lessonAssignment
        LessonAssignment updatedLessonAssignment = lessonAssignmentRepository.findById(lessonAssignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLessonAssignment are not directly saved in db
        em.detach(updatedLessonAssignment);
        updatedLessonAssignment.assignedAt(UPDATED_ASSIGNED_AT).dueDate(UPDATED_DUE_DATE);
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(updatedLessonAssignment);

        restLessonAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lessonAssignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLessonAssignmentToMatchAllProperties(updatedLessonAssignment);
    }

    @Test
    @Transactional
    void putNonExistingLessonAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonAssignment.setId(longCount.incrementAndGet());

        // Create the LessonAssignment
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lessonAssignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lessonAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLessonAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonAssignment.setId(longCount.incrementAndGet());

        // Create the LessonAssignment
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lessonAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLessonAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonAssignment.setId(longCount.incrementAndGet());

        // Create the LessonAssignment
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lessonAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLessonAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedLessonAssignment = lessonAssignmentRepository.saveAndFlush(lessonAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lessonAssignment using partial update
        LessonAssignment partialUpdatedLessonAssignment = new LessonAssignment();
        partialUpdatedLessonAssignment.setId(lessonAssignment.getId());

        restLessonAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLessonAssignment))
            )
            .andExpect(status().isOk());

        // Validate the LessonAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLessonAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLessonAssignment, lessonAssignment),
            getPersistedLessonAssignment(lessonAssignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateLessonAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedLessonAssignment = lessonAssignmentRepository.saveAndFlush(lessonAssignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lessonAssignment using partial update
        LessonAssignment partialUpdatedLessonAssignment = new LessonAssignment();
        partialUpdatedLessonAssignment.setId(lessonAssignment.getId());

        partialUpdatedLessonAssignment.assignedAt(UPDATED_ASSIGNED_AT).dueDate(UPDATED_DUE_DATE);

        restLessonAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLessonAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLessonAssignment))
            )
            .andExpect(status().isOk());

        // Validate the LessonAssignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLessonAssignmentUpdatableFieldsEquals(
            partialUpdatedLessonAssignment,
            getPersistedLessonAssignment(partialUpdatedLessonAssignment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLessonAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonAssignment.setId(longCount.incrementAndGet());

        // Create the LessonAssignment
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLessonAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lessonAssignmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lessonAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLessonAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonAssignment.setId(longCount.incrementAndGet());

        // Create the LessonAssignment
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lessonAssignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLessonAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lessonAssignment.setId(longCount.incrementAndGet());

        // Create the LessonAssignment
        LessonAssignmentDTO lessonAssignmentDTO = lessonAssignmentMapper.toDto(lessonAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLessonAssignmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lessonAssignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LessonAssignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLessonAssignment() throws Exception {
        // Initialize the database
        insertedLessonAssignment = lessonAssignmentRepository.saveAndFlush(lessonAssignment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lessonAssignment
        restLessonAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, lessonAssignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lessonAssignmentRepository.count();
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

    protected LessonAssignment getPersistedLessonAssignment(LessonAssignment lessonAssignment) {
        return lessonAssignmentRepository.findById(lessonAssignment.getId()).orElseThrow();
    }

    protected void assertPersistedLessonAssignmentToMatchAllProperties(LessonAssignment expectedLessonAssignment) {
        assertLessonAssignmentAllPropertiesEquals(expectedLessonAssignment, getPersistedLessonAssignment(expectedLessonAssignment));
    }

    protected void assertPersistedLessonAssignmentToMatchUpdatableProperties(LessonAssignment expectedLessonAssignment) {
        assertLessonAssignmentAllUpdatablePropertiesEquals(
            expectedLessonAssignment,
            getPersistedLessonAssignment(expectedLessonAssignment)
        );
    }
}
