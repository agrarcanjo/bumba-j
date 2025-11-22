package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentProfileDTO {

    private Long userId;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String municipalityCode;
    private UserLevel currentLevel;
    private Integer totalXp;
    private Integer currentStreak;
    private Integer dailyGoalXp;
    private LocalDate lastActivityDate;
    private ProfileStatistics statistics;
    private List<RecentActivity> recentActivities;

    @Setter
    @Getter
    public static class ProfileStatistics {

        private Integer totalLessonsCompleted;
        private Integer totalQuestionsAnswered;
        private Integer totalCorrectAnswers;
        private Double accuracyRate;
        private Integer totalAchievementsUnlocked;
        private Integer totalDaysActive;
        private Integer longestStreak;

        public ProfileStatistics() {}

        public ProfileStatistics(
            Integer totalLessonsCompleted,
            Integer totalQuestionsAnswered,
            Integer totalCorrectAnswers,
            Double accuracyRate,
            Integer totalAchievementsUnlocked,
            Integer totalDaysActive,
            Integer longestStreak
        ) {
            this.totalLessonsCompleted = totalLessonsCompleted;
            this.totalQuestionsAnswered = totalQuestionsAnswered;
            this.totalCorrectAnswers = totalCorrectAnswers;
            this.accuracyRate = accuracyRate;
            this.totalAchievementsUnlocked = totalAchievementsUnlocked;
            this.totalDaysActive = totalDaysActive;
            this.longestStreak = longestStreak;
        }
    }

    @Setter
    @Getter
    public static class RecentActivity {

        private String type;
        private String description;
        private LocalDate date;
        private Integer xpEarned;

        public RecentActivity() {}

        public RecentActivity(String type, String description, LocalDate date, Integer xpEarned) {
            this.type = type;
            this.description = description;
            this.date = date;
            this.xpEarned = xpEarned;
        }
    }
}
