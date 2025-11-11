package com.bumbatech.bumbalearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.UserAchievement} entity.
 */
@Schema(description = "UserAchievement - Conquistas desbloqueadas pelo usuário")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAchievementDTO implements Serializable {

    private Long id;

    @NotNull
    @Schema(description = "Data/hora do desbloqueio", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant unlockedAt;

    @NotNull
    private UserDTO user;

    @NotNull
    private AchievementDTO achievement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(Instant unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AchievementDTO getAchievement() {
        return achievement;
    }

    public void setAchievement(AchievementDTO achievement) {
        this.achievement = achievement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAchievementDTO)) {
            return false;
        }

        UserAchievementDTO userAchievementDTO = (UserAchievementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAchievementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAchievementDTO{" +
            "id=" + getId() +
            ", unlockedAt='" + getUnlockedAt() + "'" +
            ", user=" + getUser() +
            ", achievement=" + getAchievement() +
            "}";
    }
}
