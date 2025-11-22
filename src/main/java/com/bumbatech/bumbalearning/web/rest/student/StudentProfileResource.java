package com.bumbatech.bumbalearning.web.rest.student;

import com.bumbatech.bumbalearning.domain.Attempt;
import com.bumbatech.bumbalearning.domain.StudentLessonProgress;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import com.bumbatech.bumbalearning.repository.*;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.dto.StudentProfileDTO;
import com.bumbatech.bumbalearning.web.rest.errors.BadRequestAlertException;
import java.time.LocalDate;
import java.time.ZoneId;
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
@RequestMapping("/api/student")
public class StudentProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentProfileResource.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final StudentLessonProgressRepository studentLessonProgressRepository;
    private final AttemptRepository attemptRepository;
    private final UserAchievementRepository userAchievementRepository;

    public StudentProfileResource(
        UserRepository userRepository,
        UserProfileRepository userProfileRepository,
        StudentLessonProgressRepository studentLessonProgressRepository,
        AttemptRepository attemptRepository,
        UserAchievementRepository userAchievementRepository
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.attemptRepository = attemptRepository;
        this.userAchievementRepository = userAchievementRepository;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<StudentProfileDTO> getStudentProfile() {
        LOG.debug("REST request to get student profile");

        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));

        User user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("User profile not found"));

        StudentProfileDTO dto = new StudentProfileDTO();
        dto.setUserId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setMunicipalityCode(profile.getMunicipalityCode());
        dto.setCurrentLevel(profile.getCurrentLevel());
        dto.setTotalXp(profile.getTotalXp());
        dto.setCurrentStreak(profile.getCurrentStreak());
        dto.setDailyGoalXp(profile.getDailyGoalXp() != null ? profile.getDailyGoalXp() : 50);
        dto.setLastActivityDate(profile.getLastActivityDate());

        StudentProfileDTO.ProfileStatistics statistics = calculateStatistics(user.getId());
        dto.setStatistics(statistics);

        List<StudentProfileDTO.RecentActivity> recentActivities = getRecentActivities(user.getId());
        dto.setRecentActivities(recentActivities);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile/daily-goal")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<StudentProfileDTO> updateDailyGoal(@RequestBody UpdateDailyGoalRequest request) {
        LOG.debug("REST request to update daily goal: {}", request.getDailyGoalXp());

        if (request.getDailyGoalXp() == null || request.getDailyGoalXp() < 10 || request.getDailyGoalXp() > 500) {
            throw new BadRequestAlertException("Daily goal must be between 10 and 500 XP", "userProfile", "invaliddailygoal");
        }

        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("Current user login not found"));

        User user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("User profile not found"));

        profile.setDailyGoalXp(request.getDailyGoalXp());
        userProfileRepository.save(profile);

        return getStudentProfile();
    }

    private StudentProfileDTO.ProfileStatistics calculateStatistics(Long userId) {
        List<StudentLessonProgress> completedLessons = studentLessonProgressRepository.findByStudentIdAndStatus(
            userId,
            LessonStatus.COMPLETED
        );

        Integer totalLessonsCompleted = completedLessons.size();

        List<Attempt> attempts = attemptRepository.findByStudentId(userId);
        int totalQuestionsAnswered = attempts.size();
        Integer totalCorrectAnswers = (int) attempts.stream().filter(Attempt::getIsCorrect).count();
        double accuracyRate = totalQuestionsAnswered > 0 ? (totalCorrectAnswers.doubleValue() / totalQuestionsAnswered) * 100 : 0.0;

        Integer totalAchievementsUnlocked = (int) userAchievementRepository.countByUserId(userId);

        Integer totalDaysActive = calculateTotalDaysActive(completedLessons);
        Integer longestStreak = calculateLongestStreak(completedLessons);

        return new StudentProfileDTO.ProfileStatistics(
            totalLessonsCompleted,
            totalQuestionsAnswered,
            totalCorrectAnswers,
            Math.round(accuracyRate * 100.0) / 100.0,
            totalAchievementsUnlocked,
            totalDaysActive,
            longestStreak
        );
    }

    private Integer calculateTotalDaysActive(List<StudentLessonProgress> completedLessons) {
        return (int) completedLessons
            .stream()
            .filter(progress -> progress.getCompletedAt() != null)
            .map(progress -> progress.getCompletedAt().atZone(ZoneId.systemDefault()).toLocalDate())
            .distinct()
            .count();
    }

    private Integer calculateLongestStreak(List<StudentLessonProgress> completedLessons) {
        if (completedLessons.isEmpty()) {
            return 0;
        }

        List<LocalDate> dates = completedLessons
            .stream()
            .filter(progress -> progress.getCompletedAt() != null)
            .map(progress -> progress.getCompletedAt().atZone(ZoneId.systemDefault()).toLocalDate())
            .distinct()
            .sorted()
            .toList();

        if (dates.isEmpty()) {
            return 0;
        }

        int maxStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < dates.size(); i++) {
            if (dates.get(i).minusDays(1).equals(dates.get(i - 1))) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }

        return maxStreak;
    }

    private List<StudentProfileDTO.RecentActivity> getRecentActivities(Long userId) {
        List<StudentLessonProgress> recentProgress = studentLessonProgressRepository.findTop10ByStudentIdOrderByCompletedAtDesc(userId);

        return recentProgress
            .stream()
            .filter(progress -> progress.getCompletedAt() != null)
            .map(progress -> {
                String lessonTitle = progress.getLesson() != null ? progress.getLesson().getTitle() : "Lição";
                return new StudentProfileDTO.RecentActivity(
                    "LESSON_COMPLETED",
                    "Completou: " + lessonTitle,
                    progress.getCompletedAt().atZone(ZoneId.systemDefault()).toLocalDate(),
                    progress.getXpEarned()
                );
            })
            .collect(Collectors.toList());
    }

    @Setter
    @Getter
    public static class UpdateDailyGoalRequest {

        private Integer dailyGoalXp;
    }
}
