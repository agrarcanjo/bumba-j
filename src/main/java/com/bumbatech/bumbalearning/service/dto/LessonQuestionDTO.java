package com.bumbatech.bumbalearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.LessonQuestion} entity.
 */
@Schema(description = "LessonQuestion - Relacionamento N:N entre Lesson e Question\nDefine ordem das questões na lição")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LessonQuestionDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Schema(description = "Ordem da questão na lição", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer orderIndex;

    @NotNull
    private LessonDTO lesson;

    @NotNull
    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public LessonDTO getLesson() {
        return lesson;
    }

    public void setLesson(LessonDTO lesson) {
        this.lesson = lesson;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonQuestionDTO)) {
            return false;
        }

        LessonQuestionDTO lessonQuestionDTO = (LessonQuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, lessonQuestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonQuestionDTO{" +
            "id=" + getId() +
            ", orderIndex=" + getOrderIndex() +
            ", lesson=" + getLesson() +
            ", question=" + getQuestion() +
            "}";
    }
}
