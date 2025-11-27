package com.bumbatech.bumbalearning.service.dto.teacher;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class CreateAssignmentDTO {

    @NotNull
    private Long classRoomId;

    @NotNull
    private Long lessonId;

    @NotNull
    private Instant dueDate;

    public CreateAssignmentDTO() {}

    public CreateAssignmentDTO(Long classRoomId, Long lessonId, Instant dueDate) {
        this.classRoomId = classRoomId;
        this.lessonId = lessonId;
        this.dueDate = dueDate;
    }

    public Long getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(Long classRoomId) {
        this.classRoomId = classRoomId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }
}
