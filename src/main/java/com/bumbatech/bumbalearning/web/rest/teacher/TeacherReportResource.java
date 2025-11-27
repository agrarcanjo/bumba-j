package com.bumbatech.bumbalearning.web.rest.teacher;

import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import com.bumbatech.bumbalearning.repository.ClassMemberRepository;
import com.bumbatech.bumbalearning.repository.ClassRoomRepository;
import com.bumbatech.bumbalearning.repository.LessonAssignmentRepository;
import com.bumbatech.bumbalearning.repository.StudentLessonProgressRepository;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.dto.teacher.ClassReportDTO;
import com.bumbatech.bumbalearning.service.dto.teacher.LessonProgressDTO;
import com.bumbatech.bumbalearning.service.dto.teacher.StudentProgressDTO;
import com.bumbatech.bumbalearning.service.dto.teacher.StudentReportDTO;
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
@RequestMapping("/api/teacher/reports")
@PreAuthorize("hasAuthority('ROLE_TEACHER')")
public class TeacherReportResource {

    private static final Logger log = LoggerFactory.getLogger(TeacherReportResource.class);

    private final ClassRoomRepository classRoomRepository;
    private final ClassMemberRepository classMemberRepository;
    private final LessonAssignmentRepository lessonAssignmentRepository;
    private final StudentLessonProgressRepository studentLessonProgressRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public TeacherReportResource(
        ClassRoomRepository classRoomRepository,
        ClassMemberRepository classMemberRepository,
        LessonAssignmentRepository lessonAssignmentRepository,
        StudentLessonProgressRepository studentLessonProgressRepository,
        UserProfileRepository userProfileRepository,
        UserRepository userRepository
    ) {
        this.classRoomRepository = classRoomRepository;
        this.classMemberRepository = classMemberRepository;
        this.lessonAssignmentRepository = lessonAssignmentRepository;
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/class/{classRoomId}")
    public ResponseEntity<ClassReportDTO> getClassReport(@PathVariable Long classRoomId) {
        log.debug("REST request to get class report for classroom: {}", classRoomId);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var classRoom = classRoomRepository
            .findById(classRoomId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassRoom not found"));

        if (!classRoom.getTeacher().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the teacher of this classroom");
        }

        var members = classMemberRepository.findByClassRoomId(classRoomId);
        var totalStudents = members.size();
        var totalAssignments = (int) lessonAssignmentRepository.countByClassRoomId(classRoomId);

        var studentProgressList = members
            .stream()
            .map(member -> {
                var student = member.getStudent();
                var completedLessons = (int) studentLessonProgressRepository.countCompletedLessonsByUserId(student.getId());
                var averageScore = studentLessonProgressRepository.findAverageScoreByStudentId(student.getId());
                var profile = userProfileRepository.findByUserId(student.getId()).orElse(null);
                var currentStreak = profile != null ? profile.getCurrentStreak() : 0;

                return new StudentProgressDTO(
                    student.getId(),
                    student.getLogin(),
                    student.getFirstName(),
                    student.getLastName(),
                    completedLessons,
                    totalAssignments,
                    averageScore != null ? averageScore : 0.0,
                    currentStreak
                );
            })
            .collect(Collectors.toList());

        var averageProgress = totalAssignments > 0
            ? studentProgressList
                .stream()
                .mapToDouble(sp -> ((double) sp.getCompletedLessons() / totalAssignments) * 100)
                .average()
                .orElse(0.0)
            : 0.0;

        var averageScore = studentLessonProgressRepository.findAverageScoreByClassRoomId(classRoomId);

        var report = new ClassReportDTO(
            classRoom.getId(),
            classRoom.getName(),
            classRoom.getDescription(),
            totalStudents,
            totalAssignments,
            averageProgress,
            averageScore != null ? averageScore : 0.0,
            studentProgressList
        );

        return ResponseEntity.ok(report);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentReportDTO> getStudentReport(
        @PathVariable Long studentId,
        @RequestParam(required = false) Long classRoomId
    ) {
        log.debug("REST request to get student report for student: {}", studentId);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var student = userRepository
            .findById(studentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        if (classRoomId != null) {
            var classRoom = classRoomRepository
                .findById(classRoomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassRoom not found"));

            if (!classRoom.getTeacher().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the teacher of this classroom");
            }

            var isMember = classMemberRepository
                .findByClassRoomId(classRoomId)
                .stream()
                .anyMatch(member -> member.getStudent().getId().equals(studentId));

            if (!isMember) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Student is not a member of this classroom");
            }
        }

        var profile = userProfileRepository.findByUserId(studentId).orElse(null);
        var completedLessons = (int) studentLessonProgressRepository.countCompletedLessonsByUserId(studentId);
        var averageScore = studentLessonProgressRepository.findAverageScoreByStudentId(studentId);

        var lessonProgressList = studentLessonProgressRepository
            .findByStudentIdAndStatusOrderByCompletedAtDesc(studentId, LessonStatus.COMPLETED)
            .stream()
            .map(progress ->
                new LessonProgressDTO(
                    progress.getLesson().getId(),
                    progress.getLesson().getTitle(),
                    progress.getCompletedAt(),
                    progress.getScore(),
                    progress.getXpEarned() != null ? progress.getXpEarned() : 0,
                    0
                )
            )
            .collect(Collectors.toList());

        var report = new StudentReportDTO(
            student.getId(),
            student.getLogin(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            profile != null ? profile.getTotalXp() : 0,
            profile != null ? profile.getCurrentLevel().name() : "BEGINNER",
            profile != null ? profile.getCurrentStreak() : 0,
            completedLessons,
            averageScore != null ? averageScore : 0.0,
            lessonProgressList
        );

        return ResponseEntity.ok(report);
    }
}
