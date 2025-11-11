package com.bumbatech.bumbalearning.web.rest;

import static com.bumbatech.bumbalearning.domain.ClassMemberAsserts.*;
import static com.bumbatech.bumbalearning.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bumbatech.bumbalearning.IntegrationTest;
import com.bumbatech.bumbalearning.domain.ClassMember;
import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.ClassMemberRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.service.ClassMemberService;
import com.bumbatech.bumbalearning.service.dto.ClassMemberDTO;
import com.bumbatech.bumbalearning.service.mapper.ClassMemberMapper;
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
 * Integration tests for the {@link ClassMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClassMemberResourceIT {

    private static final Instant DEFAULT_ENROLLED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENROLLED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/class-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClassMemberRepository classMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ClassMemberRepository classMemberRepositoryMock;

    @Autowired
    private ClassMemberMapper classMemberMapper;

    @Mock
    private ClassMemberService classMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassMemberMockMvc;

    private ClassMember classMember;

    private ClassMember insertedClassMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassMember createEntity(EntityManager em) {
        ClassMember classMember = new ClassMember().enrolledAt(DEFAULT_ENROLLED_AT);
        // Add required entity
        ClassRoom classRoom;
        if (TestUtil.findAll(em, ClassRoom.class).isEmpty()) {
            classRoom = ClassRoomResourceIT.createEntity(em);
            em.persist(classRoom);
            em.flush();
        } else {
            classRoom = TestUtil.findAll(em, ClassRoom.class).get(0);
        }
        classMember.setClassRoom(classRoom);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        classMember.setStudent(user);
        return classMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassMember createUpdatedEntity(EntityManager em) {
        ClassMember updatedClassMember = new ClassMember().enrolledAt(UPDATED_ENROLLED_AT);
        // Add required entity
        ClassRoom classRoom;
        if (TestUtil.findAll(em, ClassRoom.class).isEmpty()) {
            classRoom = ClassRoomResourceIT.createUpdatedEntity(em);
            em.persist(classRoom);
            em.flush();
        } else {
            classRoom = TestUtil.findAll(em, ClassRoom.class).get(0);
        }
        updatedClassMember.setClassRoom(classRoom);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedClassMember.setStudent(user);
        return updatedClassMember;
    }

    @BeforeEach
    void initTest() {
        classMember = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedClassMember != null) {
            classMemberRepository.delete(insertedClassMember);
            insertedClassMember = null;
        }
    }

    @Test
    @Transactional
    void createClassMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClassMember
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);
        var returnedClassMemberDTO = om.readValue(
            restClassMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classMemberDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClassMemberDTO.class
        );

        // Validate the ClassMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClassMember = classMemberMapper.toEntity(returnedClassMemberDTO);
        assertClassMemberUpdatableFieldsEquals(returnedClassMember, getPersistedClassMember(returnedClassMember));

        insertedClassMember = returnedClassMember;
    }

    @Test
    @Transactional
    void createClassMemberWithExistingId() throws Exception {
        // Create the ClassMember with an existing ID
        classMember.setId(1L);
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnrolledAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        classMember.setEnrolledAt(null);

        // Create the ClassMember, which fails.
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        restClassMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classMemberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClassMembers() throws Exception {
        // Initialize the database
        insertedClassMember = classMemberRepository.saveAndFlush(classMember);

        // Get all the classMemberList
        restClassMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].enrolledAt").value(hasItem(DEFAULT_ENROLLED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(classMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(classMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClassMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(classMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClassMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(classMemberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getClassMember() throws Exception {
        // Initialize the database
        insertedClassMember = classMemberRepository.saveAndFlush(classMember);

        // Get the classMember
        restClassMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, classMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classMember.getId().intValue()))
            .andExpect(jsonPath("$.enrolledAt").value(DEFAULT_ENROLLED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClassMember() throws Exception {
        // Get the classMember
        restClassMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClassMember() throws Exception {
        // Initialize the database
        insertedClassMember = classMemberRepository.saveAndFlush(classMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classMember
        ClassMember updatedClassMember = classMemberRepository.findById(classMember.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClassMember are not directly saved in db
        em.detach(updatedClassMember);
        updatedClassMember.enrolledAt(UPDATED_ENROLLED_AT);
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(updatedClassMember);

        restClassMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClassMemberToMatchAllProperties(updatedClassMember);
    }

    @Test
    @Transactional
    void putNonExistingClassMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classMember.setId(longCount.incrementAndGet());

        // Create the ClassMember
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classMember.setId(longCount.incrementAndGet());

        // Create the ClassMember
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(classMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classMember.setId(longCount.incrementAndGet());

        // Create the ClassMember
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(classMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassMemberWithPatch() throws Exception {
        // Initialize the database
        insertedClassMember = classMemberRepository.saveAndFlush(classMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classMember using partial update
        ClassMember partialUpdatedClassMember = new ClassMember();
        partialUpdatedClassMember.setId(classMember.getId());

        restClassMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassMember))
            )
            .andExpect(status().isOk());

        // Validate the ClassMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClassMember, classMember),
            getPersistedClassMember(classMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateClassMemberWithPatch() throws Exception {
        // Initialize the database
        insertedClassMember = classMemberRepository.saveAndFlush(classMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the classMember using partial update
        ClassMember partialUpdatedClassMember = new ClassMember();
        partialUpdatedClassMember.setId(classMember.getId());

        partialUpdatedClassMember.enrolledAt(UPDATED_ENROLLED_AT);

        restClassMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClassMember))
            )
            .andExpect(status().isOk());

        // Validate the ClassMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClassMemberUpdatableFieldsEquals(partialUpdatedClassMember, getPersistedClassMember(partialUpdatedClassMember));
    }

    @Test
    @Transactional
    void patchNonExistingClassMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classMember.setId(longCount.incrementAndGet());

        // Create the ClassMember
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classMember.setId(longCount.incrementAndGet());

        // Create the ClassMember
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(classMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        classMember.setId(longCount.incrementAndGet());

        // Create the ClassMember
        ClassMemberDTO classMemberDTO = classMemberMapper.toDto(classMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(classMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassMember() throws Exception {
        // Initialize the database
        insertedClassMember = classMemberRepository.saveAndFlush(classMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the classMember
        restClassMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, classMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return classMemberRepository.count();
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

    protected ClassMember getPersistedClassMember(ClassMember classMember) {
        return classMemberRepository.findById(classMember.getId()).orElseThrow();
    }

    protected void assertPersistedClassMemberToMatchAllProperties(ClassMember expectedClassMember) {
        assertClassMemberAllPropertiesEquals(expectedClassMember, getPersistedClassMember(expectedClassMember));
    }

    protected void assertPersistedClassMemberToMatchUpdatableProperties(ClassMember expectedClassMember) {
        assertClassMemberAllUpdatablePropertiesEquals(expectedClassMember, getPersistedClassMember(expectedClassMember));
    }
}
