package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.ClassRoom} entity.
 */
@Schema(description = "ClassRoom - Turma gerenciada por professor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassRoomDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    @Schema(description = "Nome da turma", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull
    @Schema(description = "Idioma da turma (fase 1: apenas inglês)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Language language;

    @Schema(description = "Data de criação", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

    @Size(max = 500)
    @Schema(description = "Descrição da turma")
    private String description;

    private UserDTO teacher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(UserDTO teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassRoomDTO)) {
            return false;
        }

        ClassRoomDTO classRoomDTO = (ClassRoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classRoomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassRoomDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", language='" + getLanguage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", description='" + getDescription() + "'" +
            ", teacher=" + getTeacher() +
            "}";
    }
}
