package com.bumbatech.bumbalearning.web.rest.student;

import com.bumbatech.bumbalearning.domain.Attempt;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.repository.AttemptRepository;
import com.bumbatech.bumbalearning.repository.UserAchievementRepository;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.custom.StreakService;
import com.bumbatech.bumbalearning.service.custom.XpService;
import com.bumbatech.bumbalearning.service.dto.DashboardDTO;
import com.bumbatech.bumbalearning.service.dto.UserAchievementDTO;
import com.bumbatech.bumbalearning.service.mapper.UserAchievementMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class DashboardResource {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardResource.class);
    private static final int DAILY_GOAL_XP = 50;

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AttemptRepository attemptRepository;
    private final XpService xpService;
    private final StreakService streakService;
    private final UserAchievementMapper userAchievementMapper;

    public DashboardResource(
        UserRepository userRepository,
        UserProfileRepository userProfileRepository,
        UserAchievementRepository userAchievementRepository,
        AttemptRepository attemptRepository,
        XpService xpService,
        StreakService streakService,
        UserAchievementMapper userAchievementMapper
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.attemptRepository = attemptRepository;
        this.xpService = xpService;
        this.streakService = streakService;
        this.userAchievementMapper = userAchievementMapper;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<DashboardDTO> getDashboard() {
        LOG.debug("REST request to get student dashboard");

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User not authenticated"));

        User user = userRepository.findOneByLogin(userLogin).orElseThrow(() -> new RuntimeException("User not found: " + userLogin));

        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + user.getId()));

        DashboardDTO dashboard = new DashboardDTO();

        dashboard.setUserId(user.getId());
        dashboard.setUserName(user.getFirstName() != null ? user.getFirstName() : user.getLogin());

        Integer totalXp = profile.getTotalXp() != null ? profile.getTotalXp() : 0;
        dashboard.setTotalXp(totalXp);
        dashboard.setCurrentLevel(profile.getCurrentLevel());

        Integer currentStreak = streakService.getCurrentStreak(user);
        dashboard.setCurrentStreak(currentStreak);

        Integer xpForNextLevel = xpService.getXpForNextLevel(totalXp);
        dashboard.setXpForNextLevel(xpForNextLevel);

        Double progressToNextLevel = xpService.getProgressToNextLevel(totalXp);
        dashboard.setProgressToNextLevel(progressToNextLevel);

        dashboard.setDailyGoal(DAILY_GOAL_XP);

        Integer dailyProgress = calculateDailyProgress(user.getId());
        dashboard.setDailyProgress(dailyProgress);

        Long totalLessonsCompleted = attemptRepository.countDistinctLessonsByUserId(user.getId());
        dashboard.setTotalLessonsCompleted(totalLessonsCompleted);

        Long totalQuestionsAnswered = attemptRepository.countByStudentId(user.getId());
        dashboard.setTotalQuestionsAnswered(totalQuestionsAnswered);

        Long correctAnswers = attemptRepository.countByStudentIdAndIsCorrect(user.getId(), true);
        dashboard.setCorrectAnswers(correctAnswers);

        Double accuracyRate = totalQuestionsAnswered > 0
            ? (correctAnswers.doubleValue() / totalQuestionsAnswered.doubleValue()) * 100
            : 0.0;
        dashboard.setAccuracyRate(Math.round(accuracyRate * 100.0) / 100.0);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "unlockedAt"));
        List<UserAchievementDTO> recentAchievements = userAchievementRepository
            .findByUserId(user.getId(), pageable)
            .stream()
            .map(userAchievementMapper::toDto)
            .toList();
        dashboard.setRecentAchievements(recentAchievements);

        LOG.debug("Dashboard data retrieved for user: {}", userLogin);
        return ResponseEntity.ok(dashboard);
    }

    private Integer calculateDailyProgress(Long userId) {
        LocalDate today = LocalDate.now();
        Instant startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Attempt> dailyAttempts = attemptRepository.findByUserIdAndAttemptedAtBetween(userId, startOfDay, endOfDay);

        int dailyXp = 0;
        for (Attempt attempt : dailyAttempts) {
            if (attempt.getIsCorrect()) {
                dailyXp += xpService.calculateXpForQuestion(attempt.getQuestion(), true);
            }
        }

        return dailyXp;
    }
}
