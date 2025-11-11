package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.Lesson} entity.
 */
@Schema(description = "Lesson - Lição (agrupador de 5-15 questões)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LessonDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    @Schema(description = "Título da lição", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotNull
    @Schema(description = "Idioma da lição", requiredMode = Schema.RequiredMode.REQUIRED)
    private Language language;

    @NotNull
    @Schema(description = "Nível de dificuldade", requiredMode = Schema.RequiredMode.REQUIRED)
    private DifficultyLevel level;

    @NotNull
    @Min(value = 0)
    @Schema(description = "XP ao completar (padrão: 100)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer xpReward;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Schema(description = "% mínimo de acerto (padrão: 70)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer passThreshold;

    @NotNull
    @Schema(description = "Data de criação", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Size(max = 250)
    @Schema(description = "Descrição da lição")
    private String description;

    @NotNull
    private TopicDTO topic;

    @NotNull
    private UserDTO createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public Integer getXpReward() {
        return xpReward;
    }

    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }

    public Integer getPassThreshold() {
        return passThreshold;
    }

    public void setPassThreshold(Integer passThreshold) {
        this.passThreshold = passThreshold;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TopicDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicDTO topic) {
        this.topic = topic;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonDTO)) {
            return false;
        }

        LessonDTO lessonDTO = (LessonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lessonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", language='" + getLanguage() + "'" +
            ", level='" + getLevel() + "'" +
            ", xpReward=" + getXpReward() +
            ", passThreshold=" + getPassThreshold() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", description='" + getDescription() + "'" +
            ", topic=" + getTopic() +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
