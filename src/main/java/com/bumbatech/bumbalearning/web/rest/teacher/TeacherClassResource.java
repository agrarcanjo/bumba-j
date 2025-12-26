package com.bumbatech.bumbalearning.web.rest.teacher;

import com.bumbatech.bumbalearning.repository.*;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/classes")
public class TeacherClassResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherClassResource.class);

    private final ClassRoomRepository classRoomRepository;
    private final ClassMemberRepository classMemberRepository;
    private final LessonAssignmentRepository lessonAssignmentRepository;
    private final StudentLessonProgressRepository studentLessonProgressRepository;
    private final UserRepository userRepository;

    public TeacherClassResource(
        ClassRoomRepository classRoomRepository,
        ClassMemberRepository classMemberRepository,
        LessonAssignmentRepository lessonAssignmentRepository,
        StudentLessonProgressRepository studentLessonProgressRepository,
        UserRepository userRepository
    ) {
        this.classRoomRepository = classRoomRepository;
        this.classMemberRepository = classMemberRepository;
        this.lessonAssignmentRepository = lessonAssignmentRepository;
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<List<ClassRoomDTO>> getMyClasses() {
        LOG.debug("REST request to get Teacher's classes");

        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));

        var user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));

        var classes = classRoomRepository.findByTeacherId(user.getId());

        var classDTOs = classes
            .stream()
            .map(classRoom -> {
                var dto = new ClassRoomDTO();
                dto.setId(classRoom.getId());
                dto.setName(classRoom.getName());
                dto.setDescription(classRoom.getDescription());
                dto.setLanguage(classRoom.getLanguage());
                dto.setCreatedAt(classRoom.getCreatedAt());
                return dto;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(classDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<ClassDetailDTO> getClassDetail(@PathVariable Long id) {
        LOG.debug("REST request to get Class detail: {}", id);

        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));

        var user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));

        var classRoom = classRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));

        if (!classRoom.getTeacher().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to class");
        }

        var members = classMemberRepository.findByClassRoomId(id);
        var assignments = lessonAssignmentRepository.findByClassRoomId(id);

        var studentDTOs = members
            .stream()
            .map(member -> {
                var completedLessons = studentLessonProgressRepository.countCompletedLessonsByUserId(member.getStudent().getId());

                return new StudentSummaryDTO(
                    member.getStudent().getId(),
                    member.getStudent().getLogin(),
                    member.getStudent().getFirstName(),
                    member.getStudent().getLastName(),
                    member.getEnrolledAt(),
                    completedLessons
                );
            })
            .collect(Collectors.toList());

        var assignmentDTOs = assignments
            .stream()
            .map(assignment -> {
                var dto = new LessonAssignmentDTO();
                dto.setId(assignment.getId());
                dto.setAssignedAt(assignment.getAssignedAt());
                dto.setDueDate(assignment.getDueDate());
                return dto;
            })
            .collect(Collectors.toList());

        var detail = new ClassDetailDTO(
            classRoom.getId(),
            classRoom.getName(),
            classRoom.getDescription(),
            classRoom.getLanguage().toString(),
            classRoom.getCreatedAt(),
            studentDTOs,
            assignmentDTOs
        );

        return ResponseEntity.ok(detail);
    }

    @Setter
    @Getter
    public static class ClassDetailDTO {

        private Long id;
        private String name;
        private String description;
        private String language;
        private java.time.Instant createdAt;
        private List<StudentSummaryDTO> students;
        private List<LessonAssignmentDTO> assignments;

        public ClassDetailDTO(
            Long id,
            String name,
            String description,
            String language,
            java.time.Instant createdAt,
            List<StudentSummaryDTO> students,
            List<LessonAssignmentDTO> assignments
        ) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.language = language;
            this.createdAt = createdAt;
            this.students = students;
            this.assignments = assignments;
        }
    }

    @Setter
    @Getter
    public static class StudentSummaryDTO {

        private Long id;
        private String login;
        private String firstName;
        private String lastName;
        private java.time.Instant joinedAt;
        private long completedLessons;

        public StudentSummaryDTO(
            Long id,
            String login,
            String firstName,
            String lastName,
            java.time.Instant joinedAt,
            long completedLessons
        ) {
            this.id = id;
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.joinedAt = joinedAt;
            this.completedLessons = completedLessons;
        }
    }

    @GetMapping("/{id}/available-students")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<List<AvailableStudentDTO>> getAvailableStudents(@PathVariable Long id) {
        LOG.debug("REST request to get available students for class: {}", id);

        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));
        var user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));
        var classRoom = classRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));

        if (!classRoom.getTeacher().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to class");
        }

        var enrolledStudentIds = classMemberRepository
            .findByClassRoomId(id)
            .stream()
            .map(member -> member.getStudent().getId())
            .collect(Collectors.toSet());

        var allStudents = userRepository.findAllStudentsWithAuthorities();
        var availableStudents = allStudents
            .stream()
            .filter(student -> !enrolledStudentIds.contains(student.getId()))
            .map(student ->
                new AvailableStudentDTO(
                    student.getId(),
                    student.getLogin(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail()
                )
            )
            .collect(Collectors.toList());

        return ResponseEntity.ok(availableStudents);
    }

    @PostMapping("/{id}/add-students")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<Void> addStudentsToClass(@PathVariable Long id, @RequestBody AddStudentsRequest request) {
        LOG.debug("REST request to add students to class: {}", id);

        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));
        var user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));
        var classRoom = classRoomRepository.findById(id).orElseThrow(() -> new RuntimeException("Class not found"));

        if (!classRoom.getTeacher().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to class");
        }

        for (Long studentId : request.getStudentIds()) {
            var student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

            var existingMember = classMemberRepository.findByClassRoomIdAndStudentId(id, studentId);
            if (existingMember.isEmpty()) {
                var member = new com.bumbatech.bumbalearning.domain.ClassMember();
                member.setClassRoom(classRoom);
                member.setStudent(student);
                member.setEnrolledAt(java.time.Instant.now());
                classMemberRepository.save(member);
            }
        }

        return ResponseEntity.ok().build();
    }

    @Setter
    @Getter
    public static class AvailableStudentDTO {

        private Long id;
        private String login;
        private String firstName;
        private String lastName;
        private String email;

        public AvailableStudentDTO(Long id, String login, String firstName, String lastName, String email) {
            this.id = id;
            this.login = login;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }
    }

    @Setter
    @Getter
    public static class AddStudentsRequest {

        private List<Long> studentIds;
    }
}
