package com.bumbatech.bumbalearning.service.custom;

import com.bumbatech.bumbalearning.domain.Achievement;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserAchievement;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.repository.AchievementRepository;
import com.bumbatech.bumbalearning.repository.AttemptRepository;
import com.bumbatech.bumbalearning.repository.StudentLessonProgressRepository;
import com.bumbatech.bumbalearning.repository.UserAchievementRepository;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AchievementCheckService {

    private static final Logger log = LoggerFactory.getLogger(AchievementCheckService.class);

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserProfileRepository userProfileRepository;
    private final StudentLessonProgressRepository studentLessonProgressRepository;
    private final AttemptRepository attemptRepository;

    public AchievementCheckService(
        AchievementRepository achievementRepository,
        UserAchievementRepository userAchievementRepository,
        UserProfileRepository userProfileRepository,
        StudentLessonProgressRepository studentLessonProgressRepository,
        AttemptRepository attemptRepository
    ) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.userProfileRepository = userProfileRepository;
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.attemptRepository = attemptRepository;
    }

    public List<UserAchievement> checkAllAchievements(User user) {
        List<UserAchievement> newAchievements = new ArrayList<>();

        newAchievements.addAll(checkLessonAchievements(user));
        newAchievements.addAll(checkXpAchievements(user));
        newAchievements.addAll(checkStreakAchievements(user));

        if (!newAchievements.isEmpty()) {
            log.info("User {} unlocked {} new achievement(s)", user.getLogin(), newAchievements.size());
        }

        return newAchievements;
    }

    public List<UserAchievement> checkLessonAchievements(User user) {
        List<UserAchievement> newAchievements = new ArrayList<>();

        long completedLessons = studentLessonProgressRepository.countCompletedLessonsByUserId(user.getId());

        if (completedLessons >= 1) {
            unlockAchievement(user, "FIRST_LESSON").ifPresent(newAchievements::add);
        }
        if (completedLessons >= 10) {
            unlockAchievement(user, "LESSON_MASTER_10").ifPresent(newAchievements::add);
        }
        if (completedLessons >= 50) {
            unlockAchievement(user, "LESSON_MASTER_50").ifPresent(newAchievements::add);
        }

        return newAchievements;
    }

    public List<UserAchievement> checkXpAchievements(User user) {
        List<UserAchievement> newAchievements = new ArrayList<>();

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);

        if (profile == null) {
            return newAchievements;
        }

        int totalXp = profile.getTotalXp() != null ? profile.getTotalXp() : 0;

        if (totalXp >= 100) {
            unlockAchievement(user, "XP_COLLECTOR_100").ifPresent(newAchievements::add);
        }
        if (totalXp >= 1000) {
            unlockAchievement(user, "XP_COLLECTOR_1000").ifPresent(newAchievements::add);
        }
        if (totalXp >= 5000) {
            unlockAchievement(user, "XP_COLLECTOR_5000").ifPresent(newAchievements::add);
        }

        return newAchievements;
    }

    public List<UserAchievement> checkStreakAchievements(User user) {
        List<UserAchievement> newAchievements = new ArrayList<>();

        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);

        if (profile == null) {
            return newAchievements;
        }

        int currentStreak = profile.getCurrentStreak() != null ? profile.getCurrentStreak() : 0;

        if (currentStreak >= 7) {
            unlockAchievement(user, "STREAK_WARRIOR_7").ifPresent(newAchievements::add);
        }
        if (currentStreak >= 30) {
            unlockAchievement(user, "STREAK_WARRIOR_30").ifPresent(newAchievements::add);
        }
        if (currentStreak >= 100) {
            unlockAchievement(user, "STREAK_WARRIOR_100").ifPresent(newAchievements::add);
        }

        return newAchievements;
    }

    public Optional<UserAchievement> checkPerfectScoreAchievement(User user, int score) {
        if (score == 100) {
            return unlockAchievement(user, "PERFECT_SCORE");
        }
        return Optional.empty();
    }

    public Optional<UserAchievement> unlockAchievement(User user, String achievementCode) {
        Optional<Achievement> achievementOpt = achievementRepository.findByCode(achievementCode);

        if (achievementOpt.isEmpty()) {
            log.warn("Achievement with code {} not found", achievementCode);
            return Optional.empty();
        }

        Achievement achievement = achievementOpt.get();

        boolean alreadyUnlocked = userAchievementRepository.existsByUserIdAndAchievementId(user.getId(), achievement.getId());

        if (alreadyUnlocked) {
            log.debug("User {} already has achievement {}", user.getLogin(), achievementCode);
            return Optional.empty();
        }

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setUnlockedAt(Instant.now());

        userAchievement = userAchievementRepository.save(userAchievement);
        log.info("User {} unlocked achievement: {} - {}", user.getLogin(), achievementCode, achievement.getName());

        return Optional.of(userAchievement);
    }
}
