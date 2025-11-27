package com.bumbatech.bumbalearning.domain;

import com.bumbatech.bumbalearning.domain.enumeration.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * ClassRoom - Turma gerenciada por professor
 */
@Entity
@Table(name = "class_room")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Nome da turma
     */
    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    /**
     * Idioma da turma (fase 1: apenas inglês)
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    /**
     * Data de criação
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Descrição da turma
     */
    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private User teacher;

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClassRoom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ClassRoom name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return this.language;
    }

    public ClassRoom language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public ClassRoom createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return this.description;
    }

    public ClassRoom description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getTeacher() {
        return this.teacher;
    }

    public void setTeacher(User user) {
        this.teacher = user;
    }

    public ClassRoom teacher(User user) {
        this.setTeacher(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassRoom)) {
            return false;
        }
        return getId() != null && getId().equals(((ClassRoom) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassRoom{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", language='" + getLanguage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
