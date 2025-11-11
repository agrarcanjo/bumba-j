package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.UserProfile} entity.
 */
@Schema(description = "UserProfile - Estende User do JHipster\nRelacionamento 1:1 com User")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 7)
    @Schema(description = "Código IBGE do município", requiredMode = Schema.RequiredMode.REQUIRED)
    private String municipalityCode;

    @NotNull
    @Schema(description = "Nível atual baseado em XP", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserLevel currentLevel;

    @NotNull
    @Min(value = 0)
    @Schema(description = "XP total acumulado", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalXp;

    @NotNull
    @Min(value = 0)
    @Schema(description = "Dias consecutivos de atividade", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer currentStreak;

    @NotNull
    @Min(value = 10)
    @Schema(description = "Meta diária de XP (padrão: 50)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer dailyGoalXp;

    @Schema(description = "Data da última atividade")
    private LocalDate lastActivityDate;

    @NotNull
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public UserLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(UserLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(Integer totalXp) {
        this.totalXp = totalXp;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public Integer getDailyGoalXp() {
        return dailyGoalXp;
    }

    public void setDailyGoalXp(Integer dailyGoalXp) {
        this.dailyGoalXp = dailyGoalXp;
    }

    public LocalDate getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDate lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", municipalityCode='" + getMunicipalityCode() + "'" +
            ", currentLevel='" + getCurrentLevel() + "'" +
            ", totalXp=" + getTotalXp() +
            ", currentStreak=" + getCurrentStreak() +
            ", dailyGoalXp=" + getDailyGoalXp() +
            ", lastActivityDate='" + getLastActivityDate() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
