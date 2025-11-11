package com.bumbatech.bumbalearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * ClassMember - Alunos matriculados na turma
 */
@Entity
@Table(name = "class_member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Data de matrícula
     */
    @NotNull
    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "teacher" }, allowSetters = true)
    private ClassRoom classRoom;

    @ManyToOne(optional = false)
    @NotNull
    private User student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClassMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEnrolledAt() {
        return this.enrolledAt;
    }

    public ClassMember enrolledAt(Instant enrolledAt) {
        this.setEnrolledAt(enrolledAt);
        return this;
    }

    public void setEnrolledAt(Instant enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public ClassRoom getClassRoom() {
        return this.classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public ClassMember classRoom(ClassRoom classRoom) {
        this.setClassRoom(classRoom);
        return this;
    }

    public User getStudent() {
        return this.student;
    }

    public void setStudent(User user) {
        this.student = user;
    }

    public ClassMember student(User user) {
        this.setStudent(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassMember)) {
            return false;
        }
        return getId() != null && getId().equals(((ClassMember) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassMember{" +
            "id=" + getId() +
            ", enrolledAt='" + getEnrolledAt() + "'" +
            "}";
    }
}
