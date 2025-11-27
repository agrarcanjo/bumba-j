package com.bumbatech.bumbalearning.web.rest.teacher;

import com.bumbatech.bumbalearning.repository.*;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherDashboardResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherDashboardResource.class);

    private final ClassRoomRepository classRoomRepository;
    private final ClassMemberRepository classMemberRepository;
    private final LessonAssignmentRepository lessonAssignmentRepository;
    private final StudentLessonProgressRepository studentLessonProgressRepository;
    private final UserRepository userRepository;

    public TeacherDashboardResource(
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

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public ResponseEntity<TeacherDashboardDTO> getDashboard() {
        LOG.debug("REST request to get Teacher Dashboard");

        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));

        var user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));

        var classes = classRoomRepository.findByTeacherId(user.getId());
        var totalClasses = classes.size();

        var totalStudents = classes.stream().mapToLong(classRoom -> classMemberRepository.countByClassRoomId(classRoom.getId())).sum();

        var totalAssignments = classes
            .stream()
            .mapToLong(classRoom -> lessonAssignmentRepository.countByClassRoomId(classRoom.getId()))
            .sum();

        Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        var recentActivities = studentLessonProgressRepository.findRecentActivitiesByTeacherId(user.getId(), sevenDaysAgo);

        var classesOverview = classes
            .stream()
            .map(classRoom -> {
                var studentCount = classMemberRepository.countByClassRoomId(classRoom.getId());
                var assignmentCount = lessonAssignmentRepository.countByClassRoomId(classRoom.getId());
                var completedLessons = studentLessonProgressRepository.countCompletedLessonsByClassRoomId(classRoom.getId());

                return new ClassOverviewDTO(
                    classRoom.getId(),
                    classRoom.getName(),
                    classRoom.getDescription(),
                    studentCount,
                    assignmentCount,
                    completedLessons
                );
            })
            .collect(Collectors.toList());

        var recentActivityDTOs = recentActivities
            .stream()
            .limit(10)
            .map(activity ->
                new RecentActivityDTO(
                    activity.getStudent().getLogin(),
                    activity.getLesson().getTitle(),
                    activity.getCompletedAt(),
                    activity.getScore()
                )
            )
            .collect(Collectors.toList());

        var dashboard = new TeacherDashboardDTO(totalClasses, totalStudents, totalAssignments, classesOverview, recentActivityDTOs);

        return ResponseEntity.ok(dashboard);
    }

    public static class TeacherDashboardDTO {

        private long totalClasses;
        private long totalStudents;
        private long totalAssignments;
        private List<ClassOverviewDTO> classes;
        private List<RecentActivityDTO> recentActivities;

        public TeacherDashboardDTO(
            long totalClasses,
            long totalStudents,
            long totalAssignments,
            List<ClassOverviewDTO> classes,
            List<RecentActivityDTO> recentActivities
        ) {
            this.totalClasses = totalClasses;
            this.totalStudents = totalStudents;
            this.totalAssignments = totalAssignments;
            this.classes = classes;
            this.recentActivities = recentActivities;
        }

        public long getTotalClasses() {
            return totalClasses;
        }

        public void setTotalClasses(long totalClasses) {
            this.totalClasses = totalClasses;
        }

        public long getTotalStudents() {
            return totalStudents;
        }

        public void setTotalStudents(long totalStudents) {
            this.totalStudents = totalStudents;
        }

        public long getTotalAssignments() {
            return totalAssignments;
        }

        public void setTotalAssignments(long totalAssignments) {
            this.totalAssignments = totalAssignments;
        }

        public List<ClassOverviewDTO> getClasses() {
            return classes;
        }

        public void setClasses(List<ClassOverviewDTO> classes) {
            this.classes = classes;
        }

        public List<RecentActivityDTO> getRecentActivities() {
            return recentActivities;
        }

        public void setRecentActivities(List<RecentActivityDTO> recentActivities) {
            this.recentActivities = recentActivities;
        }
    }

    public static class ClassOverviewDTO {

        private Long id;
        private String name;
        private String description;
        private long studentCount;
        private long assignmentCount;
        private long completedLessons;

        public ClassOverviewDTO(Long id, String name, String description, long studentCount, long assignmentCount, long completedLessons) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.studentCount = studentCount;
            this.assignmentCount = assignmentCount;
            this.completedLessons = completedLessons;
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

        public long getStudentCount() {
            return studentCount;
        }

        public void setStudentCount(long studentCount) {
            this.studentCount = studentCount;
        }

        public long getAssignmentCount() {
            return assignmentCount;
        }

        public void setAssignmentCount(long assignmentCount) {
            this.assignmentCount = assignmentCount;
        }

        public long getCompletedLessons() {
            return completedLessons;
        }

        public void setCompletedLessons(long completedLessons) {
            this.completedLessons = completedLessons;
        }
    }

    public static class RecentActivityDTO {

        private String studentName;
        private String lessonTitle;
        private Instant completedAt;
        private Integer score;

        public RecentActivityDTO(String studentName, String lessonTitle, Instant completedAt, Integer score) {
            this.studentName = studentName;
            this.lessonTitle = lessonTitle;
            this.completedAt = completedAt;
            this.score = score;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getLessonTitle() {
            return lessonTitle;
        }

        public void setLessonTitle(String lessonTitle) {
            this.lessonTitle = lessonTitle;
        }

        public Instant getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(Instant completedAt) {
            this.completedAt = completedAt;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }
    }
}
