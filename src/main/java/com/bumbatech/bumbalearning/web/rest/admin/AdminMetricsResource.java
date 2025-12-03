package com.bumbatech.bumbalearning.web.rest.admin;

import com.bumbatech.bumbalearning.repository.*;
import com.bumbatech.bumbalearning.security.AuthoritiesConstants;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/metrics")
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminMetricsResource {

    private static final Logger log = LoggerFactory.getLogger(AdminMetricsResource.class);

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final AttemptRepository attemptRepository;
    private final AchievementRepository achievementRepository;

    public AdminMetricsResource(
        UserRepository userRepository,
        LessonRepository lessonRepository,
        QuestionRepository questionRepository,
        ClassRoomRepository classRoomRepository,
        AttemptRepository attemptRepository,
        AchievementRepository achievementRepository
    ) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.classRoomRepository = classRoomRepository;
        this.attemptRepository = attemptRepository;
        this.achievementRepository = achievementRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getGlobalMetrics() {
        log.debug("REST request to get global metrics");

        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalUsers", userRepository.count());
        metrics.put("totalStudents", userRepository.countByAuthoritiesName("ROLE_STUDENT"));
        metrics.put("totalTeachers", userRepository.countByAuthoritiesName("ROLE_TEACHER"));
        metrics.put("totalLessons", lessonRepository.count());
        metrics.put("totalQuestions", questionRepository.count());
        metrics.put("totalClassrooms", classRoomRepository.count());
        metrics.put("totalAttempts", attemptRepository.count());
        metrics.put("totalAchievements", achievementRepository.count());

        var activeUsers = userRepository.countByActivatedIsTrue();
        metrics.put("activeUsers", activeUsers);

        var completedAttempts = attemptRepository.countByIsCorrectIsTrue();
        metrics.put("completedAttempts", completedAttempts);

        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUserMetrics() {
        log.debug("REST request to get user metrics");

        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalUsers", userRepository.count());
        metrics.put("activeUsers", userRepository.countByActivatedIsTrue());
        metrics.put("inactiveUsers", userRepository.countByActivatedIsFalse());
        metrics.put("students", userRepository.countByAuthoritiesName("ROLE_STUDENT"));
        metrics.put("teachers", userRepository.countByAuthoritiesName("ROLE_TEACHER"));
        metrics.put("admins", userRepository.countByAuthoritiesName("ROLE_ADMIN"));

        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/content")
    public ResponseEntity<Map<String, Object>> getContentMetrics() {
        log.debug("REST request to get content metrics");

        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalLessons", lessonRepository.count());
        metrics.put("totalQuestions", questionRepository.count());
        metrics.put("totalClassrooms", classRoomRepository.count());
        metrics.put("totalAchievements", achievementRepository.count());

        var avgQuestionsPerLesson = questionRepository.count() / Math.max(lessonRepository.count(), 1);
        metrics.put("avgQuestionsPerLesson", avgQuestionsPerLesson);

        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/activity")
    public ResponseEntity<Map<String, Object>> getActivityMetrics() {
        log.debug("REST request to get activity metrics");

        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalAttempts", attemptRepository.count());
        metrics.put("completedAttempts", attemptRepository.countByIsCorrectIsTrue());
        metrics.put("inProgressAttempts", attemptRepository.countByIsCorrectIsFalse());

        var completionRate = attemptRepository.count() > 0
            ? ((double) attemptRepository.countByIsCorrectIsTrue() / attemptRepository.count()) * 100
            : 0.0;
        metrics.put("completionRate", completionRate);

        return ResponseEntity.ok(metrics);
    }
}
