package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import java.io.Serializable;
import java.util.List;

public class DashboardDTO implements Serializable {

    private Long userId;
    private String userName;
    private Integer totalXp;
    private UserLevel currentLevel;
    private Integer currentStreak;
    private Integer xpForNextLevel;
    private Double progressToNextLevel;
    private Integer dailyGoal;
    private Integer dailyProgress;
    private Long totalLessonsCompleted;
    private Long totalQuestionsAnswered;
    private Long correctAnswers;
    private Double accuracyRate;
    private List<UserAchievementDTO> recentAchievements;

    public DashboardDTO() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(Integer totalXp) {
        this.totalXp = totalXp;
    }

    public UserLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(UserLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getXpForNextLevel() {
        return xpForNextLevel;
    }

    public void setXpForNextLevel(Integer xpForNextLevel) {
        this.xpForNextLevel = xpForNextLevel;
    }

    public Double getProgressToNextLevel() {
        return progressToNextLevel;
    }

    public void setProgressToNextLevel(Double progressToNextLevel) {
        this.progressToNextLevel = progressToNextLevel;
    }

    public Integer getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(Integer dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public Integer getDailyProgress() {
        return dailyProgress;
    }

    public void setDailyProgress(Integer dailyProgress) {
        this.dailyProgress = dailyProgress;
    }

    public Long getTotalLessonsCompleted() {
        return totalLessonsCompleted;
    }

    public void setTotalLessonsCompleted(Long totalLessonsCompleted) {
        this.totalLessonsCompleted = totalLessonsCompleted;
    }

    public Long getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }

    public void setTotalQuestionsAnswered(Long totalQuestionsAnswered) {
        this.totalQuestionsAnswered = totalQuestionsAnswered;
    }

    public Long getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Long correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(Double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }

    public List<UserAchievementDTO> getRecentAchievements() {
        return recentAchievements;
    }

    public void setRecentAchievements(List<UserAchievementDTO> recentAchievements) {
        this.recentAchievements = recentAchievements;
    }

    @Override
    public String toString() {
        return (
            "DashboardDTO{" +
            "userId=" +
            userId +
            ", userName='" +
            userName +
            '\'' +
            ", totalXp=" +
            totalXp +
            ", currentLevel=" +
            currentLevel +
            ", currentStreak=" +
            currentStreak +
            ", xpForNextLevel=" +
            xpForNextLevel +
            ", progressToNextLevel=" +
            progressToNextLevel +
            ", dailyGoal=" +
            dailyGoal +
            ", dailyProgress=" +
            dailyProgress +
            ", totalLessonsCompleted=" +
            totalLessonsCompleted +
            ", totalQuestionsAnswered=" +
            totalQuestionsAnswered +
            ", correctAnswers=" +
            correctAnswers +
            ", accuracyRate=" +
            accuracyRate +
            ", recentAchievements=" +
            recentAchievements +
            '}'
        );
    }
}
