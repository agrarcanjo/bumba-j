package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import java.time.LocalDate;
import java.util.List;

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

        public Integer getTotalLessonsCompleted() {
            return totalLessonsCompleted;
        }

        public void setTotalLessonsCompleted(Integer totalLessonsCompleted) {
            this.totalLessonsCompleted = totalLessonsCompleted;
        }

        public Integer getTotalQuestionsAnswered() {
            return totalQuestionsAnswered;
        }

        public void setTotalQuestionsAnswered(Integer totalQuestionsAnswered) {
            this.totalQuestionsAnswered = totalQuestionsAnswered;
        }

        public Integer getTotalCorrectAnswers() {
            return totalCorrectAnswers;
        }

        public void setTotalCorrectAnswers(Integer totalCorrectAnswers) {
            this.totalCorrectAnswers = totalCorrectAnswers;
        }

        public Double getAccuracyRate() {
            return accuracyRate;
        }

        public void setAccuracyRate(Double accuracyRate) {
            this.accuracyRate = accuracyRate;
        }

        public Integer getTotalAchievementsUnlocked() {
            return totalAchievementsUnlocked;
        }

        public void setTotalAchievementsUnlocked(Integer totalAchievementsUnlocked) {
            this.totalAchievementsUnlocked = totalAchievementsUnlocked;
        }

        public Integer getTotalDaysActive() {
            return totalDaysActive;
        }

        public void setTotalDaysActive(Integer totalDaysActive) {
            this.totalDaysActive = totalDaysActive;
        }

        public Integer getLongestStreak() {
            return longestStreak;
        }

        public void setLongestStreak(Integer longestStreak) {
            this.longestStreak = longestStreak;
        }
    }

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Integer getXpEarned() {
            return xpEarned;
        }

        public void setXpEarned(Integer xpEarned) {
            this.xpEarned = xpEarned;
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public UserLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(UserLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(Integer totalXp) {
        this.totalXp = totalXp;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getDailyGoalXp() {
        return dailyGoalXp;
    }

    public void setDailyGoalXp(Integer dailyGoalXp) {
        this.dailyGoalXp = dailyGoalXp;
    }

    public LocalDate getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public ProfileStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(ProfileStatistics statistics) {
        this.statistics = statistics;
    }

    public List<RecentActivity> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<RecentActivity> recentActivities) {
        this.recentActivities = recentActivities;
    }
}
