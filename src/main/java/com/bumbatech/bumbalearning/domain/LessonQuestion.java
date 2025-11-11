package com.bumbatech.bumbalearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * LessonQuestion - Relacionamento N:N entre Lesson e Question
 * Define ordem das questões na lição
 */
@Entity
@Table(name = "lesson_question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LessonQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Ordem da questão na lição
     */
    @NotNull
    @Min(value = 0)
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topic", "createdBy" }, allowSetters = true)
    private Lesson lesson;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topic", "createdBy" }, allowSetters = true)
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LessonQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public LessonQuestion orderIndex(Integer orderIndex) {
        this.setOrderIndex(orderIndex);
        return this;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LessonQuestion lesson(Lesson lesson) {
        this.setLesson(lesson);
        return this;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public LessonQuestion question(Question question) {
        this.setQuestion(question);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonQuestion)) {
            return false;
        }
        return getId() != null && getId().equals(((LessonQuestion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonQuestion{" +
            "id=" + getId() +
            ", orderIndex=" + getOrderIndex() +
            "}";
    }
}
