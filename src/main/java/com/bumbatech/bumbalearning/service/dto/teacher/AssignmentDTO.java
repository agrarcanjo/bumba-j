package com.bumbatech.bumbalearning.service.dto.teacher;

import java.time.Instant;

public class AssignmentDTO {

    private Long id;
    private Long classRoomId;
    private String classRoomName;
    private Long lessonId;
    private String lessonTitle;
    private Instant assignedAt;
    private Instant dueDate;
    private String assignedByLogin;

    public AssignmentDTO() {}

    public AssignmentDTO(
        Long id,
        Long classRoomId,
        String classRoomName,
        Long lessonId,
        String lessonTitle,
        Instant assignedAt,
        Instant dueDate,
        String assignedByLogin
    ) {
        this.id = id;
        this.classRoomId = classRoomId;
        this.classRoomName = classRoomName;
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.assignedAt = assignedAt;
        this.dueDate = dueDate;
        this.assignedByLogin = assignedByLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getAssignedByLogin() {
        return assignedByLogin;
    }

    public void setAssignedByLogin(String assignedByLogin) {
        this.assignedByLogin = assignedByLogin;
    }
}
