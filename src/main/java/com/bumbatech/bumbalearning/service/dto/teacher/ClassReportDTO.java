package com.bumbatech.bumbalearning.service.dto.teacher;

import java.util.List;

public class ClassReportDTO {

    private Long classRoomId;
    private String classRoomName;
    private String description;
    private int totalStudents;
    private int totalAssignments;
    private double averageProgress;
    private double averageScore;
    private List<StudentProgressDTO> studentProgress;

    public ClassReportDTO(
        Long classRoomId,
        String classRoomName,
        String description,
        int totalStudents,
        int totalAssignments,
        double averageProgress,
        double averageScore,
        List<StudentProgressDTO> studentProgress
    ) {
        this.classRoomId = classRoomId;
        this.classRoomName = classRoomName;
        this.description = description;
        this.totalStudents = totalStudents;
        this.totalAssignments = totalAssignments;
        this.averageProgress = averageProgress;
        this.averageScore = averageScore;
        this.studentProgress = studentProgress;
    }

    public Long getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(Long classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalAssignments() {
        return totalAssignments;
    }

    public void setTotalAssignments(int totalAssignments) {
        this.totalAssignments = totalAssignments;
    }

    public double getAverageProgress() {
        return averageProgress;
    }

    public void setAverageProgress(double averageProgress) {
        this.averageProgress = averageProgress;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public List<StudentProgressDTO> getStudentProgress() {
        return studentProgress;
    }

    public void setStudentProgress(List<StudentProgressDTO> studentProgress) {
        this.studentProgress = studentProgress;
    }
}
