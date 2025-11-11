package com.bumbatech.bumbalearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.LessonAssignment} entity.
 */
@Schema(description = "LessonAssignment - Atribuição de lição para turma")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LessonAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Data de atribuição", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant assignedAt;

    @NotNull
    @Schema(description = "Prazo para conclusão", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant dueDate;

    @NotNull
    private ClassRoomDTO classRoom;

    @NotNull
    private LessonDTO lesson;

    @NotNull
    private UserDTO assignedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ClassRoomDTO getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoomDTO classRoom) {
        this.classRoom = classRoom;
    }

    public LessonDTO getLesson() {
        return lesson;
    }

    public void setLesson(LessonDTO lesson) {
        this.lesson = lesson;
    }

    public UserDTO getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(UserDTO assignedBy) {
        this.assignedBy = assignedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonAssignmentDTO)) {
            return false;
        }

        LessonAssignmentDTO lessonAssignmentDTO = (LessonAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lessonAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonAssignmentDTO{" +
            "id=" + getId() +
            ", assignedAt='" + getAssignedAt() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", classRoom=" + getClassRoom() +
            ", lesson=" + getLesson() +
            ", assignedBy=" + getAssignedBy() +
            "}";
    }
}
