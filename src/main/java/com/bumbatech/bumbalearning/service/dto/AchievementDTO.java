package com.bumbatech.bumbalearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.Achievement} entity.
 */
@Schema(description = "Achievement - Conquistas disponíveis")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AchievementDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    @Schema(description = "Código único (ex: STREAK_7)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotNull
    @Size(max = 100)
    @Schema(description = "Nome da conquista", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 500)
    @Schema(description = "URL do ícone")
    private String iconUrl;

    @Size(max = 250)
    @Schema(description = "Descrição detalhada")
    private String description;

    @Schema(description = "JSONB - critérios para desbloquear", requiredMode = Schema.RequiredMode.REQUIRED)
    @Lob
    private String criteria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AchievementDTO)) {
            return false;
        }

        AchievementDTO achievementDTO = (AchievementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, achievementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AchievementDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", iconUrl='" + getIconUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", criteria='" + getCriteria() + "'" +
            "}";
    }
}
