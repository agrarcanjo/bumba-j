package com.bumbatech.bumbalearning.service.custom;

import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class XpService {

    private static final Logger log = LoggerFactory.getLogger(XpService.class);

    private static final int XP_BEGINNER_THRESHOLD = 0;
    private static final int XP_INTERMEDIATE_THRESHOLD = 1000;
    private static final int XP_ADVANCED_THRESHOLD = 5000;

    private static final int BASE_XP_PER_QUESTION = 10;
    private static final double EASY_MULTIPLIER = 1.0;
    private static final double MEDIUM_MULTIPLIER = 1.5;
    private static final double HARD_MULTIPLIER = 2.0;

    private final UserProfileRepository userProfileRepository;

    public XpService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public int calculateXpForQuestion(Question question, boolean isCorrect) {
        if (!isCorrect) {
            return 0;
        }

        DifficultyLevel difficulty = question.getLevel();
        double multiplier =
            switch (difficulty) {
                case EASY -> EASY_MULTIPLIER;
                case INTERMEDIATE -> MEDIUM_MULTIPLIER;
                case ADVANCED -> HARD_MULTIPLIER;
            };

        int xp = (int) (BASE_XP_PER_QUESTION * multiplier);
        log.debug("Calculated XP for question {}: {} (difficulty: {})", question.getId(), xp, difficulty);
        return xp;
    }

    public UserProfile updateUserXp(User user, int xpToAdd) {
        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + user.getId()));

        int currentXp = profile.getTotalXp() != null ? profile.getTotalXp() : 0;
        int newTotalXp = currentXp + xpToAdd;

        profile.setTotalXp(newTotalXp);

        UserLevel newLevel = calculateLevel(newTotalXp);
        if (newLevel != profile.getCurrentLevel()) {
            log.info("User {} leveled up from {} to {}", user.getLogin(), profile.getCurrentLevel(), newLevel);
            profile.setCurrentLevel(newLevel);
        }

        profile = userProfileRepository.save(profile);
        log.debug("Updated XP for user {}: +{} XP (total: {})", user.getLogin(), xpToAdd, newTotalXp);

        return profile;
    }

    public UserLevel calculateLevel(int totalXp) {
        if (totalXp >= XP_ADVANCED_THRESHOLD) {
            return UserLevel.ADVANCED;
        } else if (totalXp >= XP_INTERMEDIATE_THRESHOLD) {
            return UserLevel.INTERMEDIATE;
        } else {
            return UserLevel.BEGINNER;
        }
    }

    public int getXpForNextLevel(int currentXp) {
        UserLevel currentLevel = calculateLevel(currentXp);

        return switch (currentLevel) {
            case BEGINNER -> XP_INTERMEDIATE_THRESHOLD - currentXp;
            case INTERMEDIATE -> XP_ADVANCED_THRESHOLD - currentXp;
            case ADVANCED -> 0;
        };
    }

    public double getProgressToNextLevel(int currentXp) {
        UserLevel currentLevel = calculateLevel(currentXp);

        return switch (currentLevel) {
            case BEGINNER -> ((double) currentXp / XP_INTERMEDIATE_THRESHOLD) * 100;
            case INTERMEDIATE -> ((double) (currentXp - XP_INTERMEDIATE_THRESHOLD) / (XP_ADVANCED_THRESHOLD - XP_INTERMEDIATE_THRESHOLD)) *
            100;
            case ADVANCED -> 100.0;
        };
    }
}
