package com.bumbatech.bumbalearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * LessonAssignment - Atribuição de lição para turma
 */
@Entity
@Table(name = "lesson_assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LessonAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Data de atribuição
     */
    @NotNull
    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    /**
     * Prazo para conclusão
     */
    @NotNull
    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "teacher" }, allowSetters = true)
    private ClassRoom classRoom;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "topic", "createdBy" }, allowSetters = true)
    private Lesson lesson;

    @ManyToOne(optional = false)
    @NotNull
    private User assignedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LessonAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAssignedAt() {
        return this.assignedAt;
    }

    public LessonAssignment assignedAt(Instant assignedAt) {
        this.setAssignedAt(assignedAt);
        return this;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getDueDate() {
        return this.dueDate;
    }

    public LessonAssignment dueDate(Instant dueDate) {
        this.setDueDate(dueDate);
        return this;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public ClassRoom getClassRoom() {
        return this.classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public LessonAssignment classRoom(ClassRoom classRoom) {
        this.setClassRoom(classRoom);
        return this;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public LessonAssignment lesson(Lesson lesson) {
        this.setLesson(lesson);
        return this;
    }

    public User getAssignedBy() {
        return this.assignedBy;
    }

    public void setAssignedBy(User user) {
        this.assignedBy = user;
    }

    public LessonAssignment assignedBy(User user) {
        this.setAssignedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LessonAssignment)) {
            return false;
        }
        return getId() != null && getId().equals(((LessonAssignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonAssignment{" +
            "id=" + getId() +
            ", assignedAt='" + getAssignedAt() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            "}";
    }
}
