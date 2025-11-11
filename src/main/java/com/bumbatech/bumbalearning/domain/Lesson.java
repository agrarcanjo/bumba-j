package com.bumbatech.bumbalearning.domain;

import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Lesson - Lição (agrupador de 5-15 questões)
 */
@Entity
@Table(name = "lesson")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Título da lição
     */
    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    /**
     * Idioma da lição
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    /**
     * Nível de dificuldade
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private DifficultyLevel level;

    /**
     * XP ao completar (padrão: 100)
     */
    @NotNull
    @Min(value = 0)
    @Column(name = "xp_reward", nullable = false)
    private Integer xpReward;

    /**
     * % mínimo de acerto (padrão: 70)
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "pass_threshold", nullable = false)
    private Integer passThreshold;

    /**
     * Data de criação
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Descrição da lição
     */
    @Size(max = 250)
    @Column(name = "description", length = 250)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    private Topic topic;

    @ManyToOne(optional = false)
    @NotNull
    private User createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lesson id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Lesson title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Lesson language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public DifficultyLevel getLevel() {
        return this.level;
    }

    public Lesson level(DifficultyLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public Integer getXpReward() {
        return this.xpReward;
    }

    public Lesson xpReward(Integer xpReward) {
        this.setXpReward(xpReward);
        return this;
    }

    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }

    public Integer getPassThreshold() {
        return this.passThreshold;
    }

    public Lesson passThreshold(Integer passThreshold) {
        this.setPassThreshold(passThreshold);
        return this;
    }

    public void setPassThreshold(Integer passThreshold) {
        this.passThreshold = passThreshold;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Lesson createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return this.description;
    }

    public Lesson description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Lesson topic(Topic topic) {
        this.setTopic(topic);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Lesson createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lesson)) {
            return false;
        }
        return getId() != null && getId().equals(((Lesson) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lesson{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", language='" + getLanguage() + "'" +
            ", level='" + getLevel() + "'" +
            ", xpReward=" + getXpReward() +
            ", passThreshold=" + getPassThreshold() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
