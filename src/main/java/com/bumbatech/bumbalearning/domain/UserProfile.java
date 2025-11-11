package com.bumbatech.bumbalearning.domain;

import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * UserProfile - Estende User do JHipster
 * Relacionamento 1:1 com User
 */
@Entity
@Table(name = "user_profile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Código IBGE do município
     */
    @NotNull
    @Size(max = 7)
    @Column(name = "municipality_code", length = 7, nullable = false)
    private String municipalityCode;

    /**
     * Nível atual baseado em XP
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "current_level", nullable = false)
    private UserLevel currentLevel;

    /**
     * XP total acumulado
     */
    @NotNull
    @Min(value = 0)
    @Column(name = "total_xp", nullable = false)
    private Integer totalXp;

    /**
     * Dias consecutivos de atividade
     */
    @NotNull
    @Min(value = 0)
    @Column(name = "current_streak", nullable = false)
    private Integer currentStreak;

    /**
     * Meta diária de XP (padrão: 50)
     */
    @NotNull
    @Min(value = 10)
    @Column(name = "daily_goal_xp", nullable = false)
    private Integer dailyGoalXp;

    /**
     * Data da última atividade
     */
    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMunicipalityCode() {
        return this.municipalityCode;
    }

    public UserProfile municipalityCode(String municipalityCode) {
        this.setMunicipalityCode(municipalityCode);
        return this;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public UserLevel getCurrentLevel() {
        return this.currentLevel;
    }

    public UserProfile currentLevel(UserLevel currentLevel) {
        this.setCurrentLevel(currentLevel);
        return this;
    }

    public void setCurrentLevel(UserLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getTotalXp() {
        return this.totalXp;
    }

    public UserProfile totalXp(Integer totalXp) {
        this.setTotalXp(totalXp);
        return this;
    }

    public void setTotalXp(Integer totalXp) {
        this.totalXp = totalXp;
    }

    public Integer getCurrentStreak() {
        return this.currentStreak;
    }

    public UserProfile currentStreak(Integer currentStreak) {
        this.setCurrentStreak(currentStreak);
        return this;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getDailyGoalXp() {
        return this.dailyGoalXp;
    }

    public UserProfile dailyGoalXp(Integer dailyGoalXp) {
        this.setDailyGoalXp(dailyGoalXp);
        return this;
    }

    public void setDailyGoalXp(Integer dailyGoalXp) {
        this.dailyGoalXp = dailyGoalXp;
    }

    public LocalDate getLastActivityDate() {
        return this.lastActivityDate;
    }

    public UserProfile lastActivityDate(LocalDate lastActivityDate) {
        this.setLastActivityDate(lastActivityDate);
        return this;
    }

    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfile user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", municipalityCode='" + getMunicipalityCode() + "'" +
            ", currentLevel='" + getCurrentLevel() + "'" +
            ", totalXp=" + getTotalXp() +
            ", currentStreak=" + getCurrentStreak() +
            ", dailyGoalXp=" + getDailyGoalXp() +
            ", lastActivityDate='" + getLastActivityDate() + "'" +
            "}";
    }
}
