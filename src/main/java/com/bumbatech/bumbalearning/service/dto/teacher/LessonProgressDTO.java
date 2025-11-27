package com.bumbatech.bumbalearning.service.dto.teacher;

import java.time.Instant;

public class LessonProgressDTO {

    private Long lessonId;
    private String lessonTitle;
    private Instant completedAt;
    private int score;
    private int xpGained;
    private int attempts;

    public LessonProgressDTO() {}

    public LessonProgressDTO(Long lessonId, String lessonTitle, Instant completedAt, int score, int xpGained, int attempts) {
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.completedAt = completedAt;
        this.score = score;
        this.xpGained = xpGained;
        this.attempts = attempts;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getXpGained() {
        return xpGained;
    }

    public void setXpGained(int xpGained) {
        this.xpGained = xpGained;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
