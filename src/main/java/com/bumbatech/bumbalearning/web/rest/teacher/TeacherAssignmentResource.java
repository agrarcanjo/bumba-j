package com.bumbatech.bumbalearning.web.rest.teacher;

import com.bumbatech.bumbalearning.domain.LessonAssignment;
import com.bumbatech.bumbalearning.repository.ClassRoomRepository;
import com.bumbatech.bumbalearning.repository.LessonAssignmentRepository;
import com.bumbatech.bumbalearning.repository.LessonRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.dto.teacher.AssignmentDTO;
import com.bumbatech.bumbalearning.service.dto.teacher.CreateAssignmentDTO;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/teacher/assignments")
@PreAuthorize("hasAuthority('ROLE_TEACHER')")
public class TeacherAssignmentResource {

    private static final Logger log = LoggerFactory.getLogger(TeacherAssignmentResource.class);

    private final LessonAssignmentRepository lessonAssignmentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public TeacherAssignmentResource(
        LessonAssignmentRepository lessonAssignmentRepository,
        ClassRoomRepository classRoomRepository,
        LessonRepository lessonRepository,
        UserRepository userRepository
    ) {
        this.lessonAssignmentRepository = lessonAssignmentRepository;
        this.classRoomRepository = classRoomRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAllAssignments() {
        log.debug("REST request to get all assignments for current teacher");

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var assignments = lessonAssignmentRepository.findByTeacherId(currentUser.getId());

        var assignmentDTOs = assignments
            .stream()
            .map(assignment ->
                new AssignmentDTO(
                    assignment.getId(),
                    assignment.getClassRoom().getId(),
                    assignment.getClassRoom().getName(),
                    assignment.getLesson().getId(),
                    assignment.getLesson().getTitle(),
                    assignment.getAssignedAt(),
                    assignment.getDueDate(),
                    assignment.getAssignedBy().getLogin()
                )
            )
            .collect(Collectors.toList());

        return ResponseEntity.ok(assignmentDTOs);
    }

    @GetMapping("/classroom/{classRoomId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByClassRoom(@PathVariable Long classRoomId) {
        log.debug("REST request to get assignments for classroom: {}", classRoomId);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var classRoom = classRoomRepository
            .findById(classRoomId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassRoom not found"));

        if (!classRoom.getTeacher().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the teacher of this classroom");
        }

        var assignments = lessonAssignmentRepository.findByClassRoomIdWithDetails(classRoomId);

        var assignmentDTOs = assignments
            .stream()
            .map(assignment ->
                new AssignmentDTO(
                    assignment.getId(),
                    assignment.getClassRoom().getId(),
                    assignment.getClassRoom().getName(),
                    assignment.getLesson().getId(),
                    assignment.getLesson().getTitle(),
                    assignment.getAssignedAt(),
                    assignment.getDueDate(),
                    assignment.getAssignedBy().getLogin()
                )
            )
            .collect(Collectors.toList());

        return ResponseEntity.ok(assignmentDTOs);
    }

    @PostMapping
    public ResponseEntity<AssignmentDTO> createAssignment(@Valid @RequestBody CreateAssignmentDTO createDTO) {
        log.debug("REST request to create assignment: {}", createDTO);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var classRoom = classRoomRepository
            .findById(createDTO.getClassRoomId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassRoom not found"));

        if (!classRoom.getTeacher().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the teacher of this classroom");
        }

        var lesson = lessonRepository
            .findById(createDTO.getLessonId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        var assignment = new LessonAssignment();
        assignment.setClassRoom(classRoom);
        assignment.setLesson(lesson);
        assignment.setAssignedBy(currentUser);
        assignment.setAssignedAt(Instant.now());
        assignment.setDueDate(createDTO.getDueDate());

        var savedAssignment = lessonAssignmentRepository.save(assignment);

        var assignmentDTO = new AssignmentDTO(
            savedAssignment.getId(),
            savedAssignment.getClassRoom().getId(),
            savedAssignment.getClassRoom().getName(),
            savedAssignment.getLesson().getId(),
            savedAssignment.getLesson().getTitle(),
            savedAssignment.getAssignedAt(),
            savedAssignment.getDueDate(),
            savedAssignment.getAssignedBy().getLogin()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> updateAssignment(@PathVariable Long id, @Valid @RequestBody CreateAssignmentDTO updateDTO) {
        log.debug("REST request to update assignment: {}", id);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var assignment = lessonAssignmentRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));

        if (!assignment.getClassRoom().getTeacher().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the teacher of this classroom");
        }

        var classRoom = classRoomRepository
            .findById(updateDTO.getClassRoomId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassRoom not found"));

        if (!classRoom.getTeacher().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the teacher of this classroom");
        }

        var lesson = lessonRepository
            .findById(updateDTO.getLessonId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        assignment.setClassRoom(classRoom);
        assignment.setLesson(lesson);
        assignment.setDueDate(updateDTO.getDueDate());

        var updatedAssignment = lessonAssignmentRepository.save(assignment);

        var assignmentDTO = new AssignmentDTO(
            updatedAssignment.getId(),
            updatedAssignment.getClassRoom().getId(),
            updatedAssignment.getClassRoom().getName(),
            updatedAssignment.getLesson().getId(),
            updatedAssignment.getLesson().getTitle(),
            updatedAssignment.getAssignedAt(),
            updatedAssignment.getDueDate(),
            updatedAssignment.getAssignedBy().getLogin()
        );

        return ResponseEntity.ok(assignmentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        log.debug("REST request to delete assignment: {}", id);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var assignment = lessonAssignmentRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));

        if (!assignment.getClassRoom().getTeacher().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the teacher of this classroom");
        }

        lessonAssignmentRepository.delete(assignment);

        return ResponseEntity.noContent().build();
    }
}
