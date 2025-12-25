package com.bumbatech.bumbalearning.service.dto.teacher;

public class StudentProgressDTO {

    private Long studentId;
    private String studentLogin;
    private String firstName;
    private String lastName;
    private int completedLessons;
    private int totalAssignments;
    private double averageScore;
    private int currentStreak;

    public StudentProgressDTO(
        Long studentId,
        String studentLogin,
        String firstName,
        String lastName,
        int completedLessons,
        int totalAssignments,
        double averageScore,
        int currentStreak
    ) {
        this.studentId = studentId;
        this.studentLogin = studentLogin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.completedLessons = completedLessons;
        this.totalAssignments = totalAssignments;
        this.averageScore = averageScore;
        this.currentStreak = currentStreak;
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

    public int getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }

    public int getTotalAssignments() {
        return totalAssignments;
    }

    public void setTotalAssignments(int totalAssignments) {
        this.totalAssignments = totalAssignments;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }
}
