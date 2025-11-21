package com.bumbatech.bumbalearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Attempt - Registro de cada tentativa de resposta
 */
@Entity
@Table(name = "attempt")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Se a resposta está correta
     */
    @NotNull
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    /**
     * Tempo gasto na questão
     */
    @NotNull
    @Min(value = 0)
    @Column(name = "time_spent_seconds", nullable = false)
    private Integer timeSpentSeconds;

    /**
     * Data/hora da tentativa
     */
    @NotNull
    @Column(name = "attempted_at", nullable = false)
    private Instant attemptedAt;

    /**
     * JSONB - resposta do aluno
     */
    @Column(name = "answer", nullable = false, length = 2000)
    private String answer;

    /**
     * JSONB - dados extras (confidence, etc)
     */
    @Column(name = "metadata", length = 2000)
    private String metadata;

    @ManyToOne(optional = false)
    @NotNull
    private User student;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topic", "createdBy" }, allowSetters = true)
    private Question question;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topic", "createdBy" }, allowSetters = true)
    private Lesson lesson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attempt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsCorrect() {
        return this.isCorrect;
    }

    public Attempt isCorrect(Boolean isCorrect) {
        this.setIsCorrect(isCorrect);
        return this;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getTimeSpentSeconds() {
        return this.timeSpentSeconds;
    }

    public Attempt timeSpentSeconds(Integer timeSpentSeconds) {
        this.setTimeSpentSeconds(timeSpentSeconds);
        return this;
    }

    public void setTimeSpentSeconds(Integer timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public Instant getAttemptedAt() {
        return this.attemptedAt;
    }

    public Attempt attemptedAt(Instant attemptedAt) {
        this.setAttemptedAt(attemptedAt);
        return this;
    }

    public void setAttemptedAt(Instant attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    public String getAnswer() {
        return this.answer;
    }

    public Attempt answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public Attempt metadata(String metadata) {
        this.setMetadata(metadata);
        return this;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public User getStudent() {
        return this.student;
    }

    public void setStudent(User user) {
        this.student = user;
    }

    public Attempt student(User user) {
        this.setStudent(user);
        return this;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Attempt question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Attempt lesson(Lesson lesson) {
        this.setLesson(lesson);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attempt)) {
            return false;
        }
        return getId() != null && getId().equals(((Attempt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attempt{" +
            "id=" + getId() +
            ", isCorrect='" + getIsCorrect() + "'" +
            ", timeSpentSeconds=" + getTimeSpentSeconds() +
            ", attemptedAt='" + getAttemptedAt() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", metadata='" + getMetadata() + "'" +
            "}";
    }
}
