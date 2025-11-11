package com.bumbatech.bumbalearning.domain;

import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * StudentLessonProgress - Progresso individual do aluno em lições
 */
@Entity
@Table(name = "student_lesson_progress")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentLessonProgress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Status da lição
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LessonStatus status;

    /**
     * Percentual de acerto
     */
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "score")
    private Integer score;

    /**
     * XP ganho nesta lição
     */
    @Min(value = 0)
    @Column(name = "xp_earned")
    private Integer xpEarned;

    /**
     * Data/hora de conclusão
     */
    @Column(name = "completed_at")
    private Instant completedAt;

    /**
     * Se completou após o prazo
     */
    @Column(name = "is_late")
    private Boolean isLate;

    @ManyToOne(optional = false)
    @NotNull
    private User student;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topic", "createdBy" }, allowSetters = true)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "classRoom", "lesson", "assignedBy" }, allowSetters = true)
    private LessonAssignment assignment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentLessonProgress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LessonStatus getStatus() {
        return this.status;
    }

    public StudentLessonProgress status(LessonStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LessonStatus status) {
        this.status = status;
    }

    public Integer getScore() {
        return this.score;
    }

    public StudentLessonProgress score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getXpEarned() {
        return this.xpEarned;
    }

    public StudentLessonProgress xpEarned(Integer xpEarned) {
        this.setXpEarned(xpEarned);
        return this;
    }

    public void setXpEarned(Integer xpEarned) {
        this.xpEarned = xpEarned;
    }

    public Instant getCompletedAt() {
        return this.completedAt;
    }

    public StudentLessonProgress completedAt(Instant completedAt) {
        this.setCompletedAt(completedAt);
        return this;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public Boolean getIsLate() {
        return this.isLate;
    }

    public StudentLessonProgress isLate(Boolean isLate) {
        this.setIsLate(isLate);
        return this;
    }

    public void setIsLate(Boolean isLate) {
        this.isLate = isLate;
    }

    public User getStudent() {
        return this.student;
    }

    public void setStudent(User user) {
        this.student = user;
    }

    public StudentLessonProgress student(User user) {
        this.setStudent(user);
        return this;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public StudentLessonProgress lesson(Lesson lesson) {
        this.setLesson(lesson);
        return this;
    }

    public LessonAssignment getAssignment() {
        return this.assignment;
    }

    public void setAssignment(LessonAssignment lessonAssignment) {
        this.assignment = lessonAssignment;
    }

    public StudentLessonProgress assignment(LessonAssignment lessonAssignment) {
        this.setAssignment(lessonAssignment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentLessonProgress)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentLessonProgress) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentLessonProgress{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", score=" + getScore() +
            ", xpEarned=" + getXpEarned() +
            ", completedAt='" + getCompletedAt() + "'" +
            ", isLate='" + getIsLate() + "'" +
            "}";
    }
}
