package com.bumbatech.bumbalearning.web.rest.teacher;

import com.bumbatech.bumbalearning.repository.*;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.dto.ClassMemberDTO;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import java.util.List;
import java.util.stream.Collectors;
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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public java.time.Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(java.time.Instant createdAt) {
            this.createdAt = createdAt;
        }

        public List<StudentSummaryDTO> getStudents() {
            return students;
        }

        public void setStudents(List<StudentSummaryDTO> students) {
            this.students = students;
        }

        public List<LessonAssignmentDTO> getAssignments() {
            return assignments;
        }

        public void setAssignments(List<LessonAssignmentDTO> assignments) {
            this.assignments = assignments;
        }
    }

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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public java.time.Instant getJoinedAt() {
            return joinedAt;
        }

        public void setJoinedAt(java.time.Instant joinedAt) {
            this.joinedAt = joinedAt;
        }

        public long getCompletedLessons() {
            return completedLessons;
        }

        public void setCompletedLessons(long completedLessons) {
            this.completedLessons = completedLessons;
        }
    }
}
