package com.bumbatech.bumbalearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Recommendation - Histórico de recomendações de lições
 */
@Entity
@Table(name = "recommendation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Data/hora da recomendação
     */
    @NotNull
    @Column(name = "recommended_at", nullable = false)
    private Instant recommendedAt;

    /**
     * Se o aluno completou a lição
     */
    @NotNull
    @Column(name = "was_completed", nullable = false)
    private Boolean wasCompleted;

    /**
     * Motivo da recomendação
     */
    @Size(max = 500)
    @Column(name = "reason", length = 500)
    private String reason;

    @ManyToOne(optional = false)
    @NotNull
    private User student;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topic", "createdBy" }, allowSetters = true)
    private Lesson lesson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recommendation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRecommendedAt() {
        return this.recommendedAt;
    }

    public Recommendation recommendedAt(Instant recommendedAt) {
        this.setRecommendedAt(recommendedAt);
        return this;
    }

    public void setRecommendedAt(Instant recommendedAt) {
        this.recommendedAt = recommendedAt;
    }

    public Boolean getWasCompleted() {
        return this.wasCompleted;
    }

    public Recommendation wasCompleted(Boolean wasCompleted) {
        this.setWasCompleted(wasCompleted);
        return this;
    }

    public void setWasCompleted(Boolean wasCompleted) {
        this.wasCompleted = wasCompleted;
    }

    public String getReason() {
        return this.reason;
    }

    public Recommendation reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getStudent() {
        return this.student;
    }

    public void setStudent(User user) {
        this.student = user;
    }

    public Recommendation student(User user) {
        this.setStudent(user);
        return this;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Recommendation lesson(Lesson lesson) {
        this.setLesson(lesson);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recommendation)) {
            return false;
        }
        return getId() != null && getId().equals(((Recommendation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recommendation{" +
            "id=" + getId() +
            ", recommendedAt='" + getRecommendedAt() + "'" +
            ", wasCompleted='" + getWasCompleted() + "'" +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
