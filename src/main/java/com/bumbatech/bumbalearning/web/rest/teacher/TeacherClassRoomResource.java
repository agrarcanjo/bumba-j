package com.bumbatech.bumbalearning.web.rest.teacher;

import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.ClassRoomRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.mapper.ClassRoomMapper;
import com.bumbatech.bumbalearning.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/teacher/class-rooms")
@PreAuthorize("hasAuthority('ROLE_TEACHER')")
public class TeacherClassRoomResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherClassRoomResource.class);
    private static final String ENTITY_NAME = "classRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomMapper classRoomMapper;
    private final UserRepository userRepository;

    public TeacherClassRoomResource(
        ClassRoomRepository classRoomRepository,
        ClassRoomMapper classRoomMapper,
        UserRepository userRepository
    ) {
        this.classRoomRepository = classRoomRepository;
        this.classRoomMapper = classRoomMapper;
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public ResponseEntity<ClassRoomDTO> createClassRoom(@Valid @RequestBody ClassRoomDTO classRoomDTO) throws URISyntaxException {
        LOG.debug("REST request to save ClassRoom : {}", classRoomDTO);

        if (classRoomDTO.getId() != null) {
            throw new BadRequestAlertException("A new classRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, "usernotfound"));

        User teacher = userRepository
            .findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", ENTITY_NAME, "usernotfound"));

        ClassRoom classRoom = classRoomMapper.toEntity(classRoomDTO);
        classRoom.setTeacher(teacher);
        classRoom.setCreatedAt(Instant.now());
        classRoom = classRoomRepository.save(classRoom);

        ClassRoomDTO result = classRoomMapper.toDto(classRoom);
        return ResponseEntity.created(new URI("/api/teacher/class-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassRoomDTO> updateClassRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassRoomDTO classRoomDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClassRoom : {}, {}", id, classRoomDTO);

        if (classRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!id.equals(classRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, "usernotfound"));

        ClassRoom existingClassRoom = classRoomRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        if (!existingClassRoom.getTeacher().getLogin().equals(currentUserLogin)) {
            throw new BadRequestAlertException("You can only update your own classrooms", ENTITY_NAME, "notauthorized");
        }

        ClassRoom classRoom = classRoomMapper.toEntity(classRoomDTO);
        classRoom.setTeacher(existingClassRoom.getTeacher());
        classRoom.setCreatedAt(existingClassRoom.getCreatedAt());
        classRoom = classRoomRepository.save(classRoom);

        ClassRoomDTO result = classRoomMapper.toDto(classRoom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClassRoom : {}", id);

        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Current user login not found", ENTITY_NAME, "usernotfound"));

        ClassRoom classRoom = classRoomRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        if (!classRoom.getTeacher().getLogin().equals(currentUserLogin)) {
            throw new BadRequestAlertException("You can only delete your own classrooms", ENTITY_NAME, "notauthorized");
        }

        classRoomRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
