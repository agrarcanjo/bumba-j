package com.bumbatech.bumbalearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.Attempt} entity.
 */
@Schema(description = "Attempt - Registro de cada tentativa de resposta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttemptDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Se a resposta está correta", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isCorrect;

    @NotNull
    @Min(value = 0)
    @Schema(description = "Tempo gasto na questão", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer timeSpentSeconds;

    @NotNull
    @Schema(description = "Data/hora da tentativa", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant attemptedAt;

    @Schema(description = "JSONB - resposta do aluno", requiredMode = Schema.RequiredMode.REQUIRED)
    @Lob
    private String answer;

    @Schema(description = "JSONB - dados extras (confidence, etc)")
    @Lob
    private String metadata;

    @NotNull
    private UserDTO student;

    @NotNull
    private QuestionDTO question;

    @NotNull
    private LessonDTO lesson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(Integer timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public Instant getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(Instant attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public UserDTO getStudent() {
        return student;
    }

    public void setStudent(UserDTO student) {
        this.student = student;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
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
        if (!(o instanceof AttemptDTO)) {
            return false;
        }

        AttemptDTO attemptDTO = (AttemptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attemptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttemptDTO{" +
            "id=" + getId() +
            ", isCorrect='" + getIsCorrect() + "'" +
            ", timeSpentSeconds=" + getTimeSpentSeconds() +
            ", attemptedAt='" + getAttemptedAt() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", metadata='" + getMetadata() + "'" +
            ", student=" + getStudent() +
            ", question=" + getQuestion() +
            ", lesson=" + getLesson() +
            "}";
    }
}
