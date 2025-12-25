package com.bumbatech.bumbalearning.service.dto.teacher;

import java.util.List;

public class StudentReportDTO {

    private Long studentId;
    private String studentLogin;
    private String firstName;
    private String lastName;
    private String email;
    private int totalXp;
    private String level;
    private int currentStreak;
    private int completedLessons;
    private double averageScore;
    private List<LessonProgressDTO> lessonProgress;

    public StudentReportDTO(
        Long studentId,
        String studentLogin,
        String firstName,
        String lastName,
        String email,
        int totalXp,
        String level,
        int currentStreak,
        int completedLessons,
        double averageScore,
        List<LessonProgressDTO> lessonProgress
    ) {
        this.studentId = studentId;
        this.studentLogin = studentLogin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.totalXp = totalXp;
        this.level = level;
        this.currentStreak = currentStreak;
        this.completedLessons = completedLessons;
        this.averageScore = averageScore;
        this.lessonProgress = lessonProgress;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentLogin() {
        return studentLogin;
    }

    public void setStudentLogin(String studentLogin) {
        this.studentLogin = studentLogin;
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

    public int getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public List<LessonProgressDTO> getLessonProgress() {
        return lessonProgress;
    }

    public void setLessonProgress(List<LessonProgressDTO> lessonProgress) {
        this.lessonProgress = lessonProgress;
    }
}
