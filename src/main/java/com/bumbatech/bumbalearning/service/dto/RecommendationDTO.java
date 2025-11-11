package com.bumbatech.bumbalearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.Recommendation} entity.
 */
@Schema(description = "Recommendation - Histórico de recomendações de lições")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecommendationDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Data/hora da recomendação", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant recommendedAt;

    @NotNull
    @Schema(description = "Se o aluno completou a lição", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean wasCompleted;

    @Size(max = 500)
    @Schema(description = "Motivo da recomendação")
    private String reason;

    @NotNull
    private UserDTO student;

    @NotNull
    private LessonDTO lesson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRecommendedAt() {
        return recommendedAt;
    }

    public void setRecommendedAt(Instant recommendedAt) {
        this.recommendedAt = recommendedAt;
    }

    public Boolean getWasCompleted() {
        return wasCompleted;
    }

    public void setWasCompleted(Boolean wasCompleted) {
        this.wasCompleted = wasCompleted;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecommendationDTO)) {
            return false;
        }

        RecommendationDTO recommendationDTO = (RecommendationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recommendationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecommendationDTO{" +
            "id=" + getId() +
            ", recommendedAt='" + getRecommendedAt() + "'" +
            ", wasCompleted='" + getWasCompleted() + "'" +
            ", reason='" + getReason() + "'" +
            ", student=" + getStudent() +
            ", lesson=" + getLesson() +
            "}";
    }
}
