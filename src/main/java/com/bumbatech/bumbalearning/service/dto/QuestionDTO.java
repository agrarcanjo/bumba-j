package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import com.bumbatech.bumbalearning.domain.enumeration.QuestionType;
import com.bumbatech.bumbalearning.domain.enumeration.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.Question} entity.
 */
@Schema(description = "Question - Banco de questões\nArmazena todos os tipos de atividades")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Tipo da questão", requiredMode = Schema.RequiredMode.REQUIRED)
    private QuestionType type;

    @NotNull
    @Schema(description = "Idioma da questão", requiredMode = Schema.RequiredMode.REQUIRED)
    private Language language;

    @NotNull
    @Schema(description = "Habilidade trabalhada", requiredMode = Schema.RequiredMode.REQUIRED)
    private Skill skill;

    @NotNull
    @Schema(description = "Nível de dificuldade", requiredMode = Schema.RequiredMode.REQUIRED)
    private DifficultyLevel level;

    @NotNull
    @Schema(description = "Data de criação", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @NotNull
    @Size(max = 2000)
    @Schema(description = "Enunciado da questão", requiredMode = Schema.RequiredMode.REQUIRED)
    private String prompt;

    @Schema(description = "JSONB - estrutura específica por tipo", requiredMode = Schema.RequiredMode.REQUIRED)
    @Lob
    private String content;

    @Schema(description = "JSONB - estrutura específica por tipo")
    @Lob
    private String assets;

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

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
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
        if (!(o instanceof QuestionDTO)) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, questionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", language='" + getLanguage() + "'" +
            ", skill='" + getSkill() + "'" +
            ", level='" + getLevel() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", prompt='" + getPrompt() + "'" +
            ", content='" + getContent() + "'" +
            ", assets='" + getAssets() + "'" +
            ", topic=" + getTopic() +
            ", createdBy=" + getCreatedBy() +
            "}";
    }
}
