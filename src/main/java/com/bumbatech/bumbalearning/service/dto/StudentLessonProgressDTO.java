package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.StudentLessonProgress} entity.
 */
@Schema(description = "StudentLessonProgress - Progresso individual do aluno em lições")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentLessonProgressDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Status da lição", requiredMode = Schema.RequiredMode.REQUIRED)
    private LessonStatus status;

    @Min(value = 0)
    @Max(value = 100)
    @Schema(description = "Percentual de acerto")
    private Integer score;

    @Min(value = 0)
    @Schema(description = "XP ganho nesta lição")
    private Integer xpEarned;

    @Schema(description = "Data/hora de conclusão")
    private Instant completedAt;

    @Schema(description = "Se completou após o prazo")
    private Boolean isLate;

    @NotNull
    private UserDTO student;

    @NotNull
    private LessonDTO lesson;

    private LessonAssignmentDTO assignment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LessonStatus getStatus() {
        return status;
    }

    public void setStatus(LessonStatus status) {
        this.status = status;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getXpEarned() {
        return xpEarned;
    }

    public void setXpEarned(Integer xpEarned) {
        this.xpEarned = xpEarned;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getIsLate() {
        return isLate;
    }

    public void setIsLate(Boolean isLate) {
        this.isLate = isLate;
    }

    public UserDTO getStudent() {
        return student;
    }

    public void setStudent(UserDTO student) {
        this.student = student;
    }

    public LessonDTO getLesson() {
        return lesson;
    }

    public void setLesson(LessonDTO lesson) {
        this.lesson = lesson;
    }

    public LessonAssignmentDTO getAssignment() {
        return assignment;
    }

    public void setAssignment(LessonAssignmentDTO assignment) {
        this.assignment = assignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentLessonProgressDTO)) {
            return false;
        }

        StudentLessonProgressDTO studentLessonProgressDTO = (StudentLessonProgressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentLessonProgressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentLessonProgressDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", score=" + getScore() +
            ", xpEarned=" + getXpEarned() +
            ", completedAt='" + getCompletedAt() + "'" +
            ", isLate='" + getIsLate() + "'" +
            ", student=" + getStudent() +
            ", lesson=" + getLesson() +
            ", assignment=" + getAssignment() +
            "}";
    }
}
