package com.bumbatech.bumbalearning.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * UserAchievement - Conquistas desbloqueadas pelo usuário
 */
@Entity
@Table(name = "user_achievement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAchievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Data/hora do desbloqueio
     */
    @NotNull
    @Column(name = "unlocked_at", nullable = false)
    private Instant unlockedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    private Achievement achievement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAchievement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUnlockedAt() {
        return this.unlockedAt;
    }

    public UserAchievement unlockedAt(Instant unlockedAt) {
        this.setUnlockedAt(unlockedAt);
        return this;
    }

    public void setUnlockedAt(Instant unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAchievement user(User user) {
        this.setUser(user);
        return this;
    }

    public Achievement getAchievement() {
        return this.achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public UserAchievement achievement(Achievement achievement) {
        this.setAchievement(achievement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAchievement)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAchievement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAchievement{" +
            "id=" + getId() +
            ", unlockedAt='" + getUnlockedAt() + "'" +
            "}";
    }
}
