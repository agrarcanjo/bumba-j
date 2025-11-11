package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bumbatech.bumbalearning.domain.Topic} entity.
 */
@Schema(description = "Topic - Temas de aprendizado (normalizado para reuso)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TopicDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    @Schema(description = "Ex: \"Greetings\", \"Food\", \"Travel\"", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull
    @Schema(description = "Idioma do tema", requiredMode = Schema.RequiredMode.REQUIRED)
    private Language language;

    @Size(max = 500)
    @Schema(description = "Descrição detalhada do tema")
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopicDTO)) {
            return false;
        }

        TopicDTO topicDTO = (TopicDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, topicDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", language='" + getLanguage() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
