package com.bumbatech.bumbalearning.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * Achievement - Conquistas disponíveis
 */
@Entity
@Table(name = "achievement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Achievement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Código único (ex: STREAK_7)
     */
    @NotNull
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;

    /**
     * Nome da conquista
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * URL do ícone
     */
    @Size(max = 500)
    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    /**
     * Descrição detalhada
     */
    @Size(max = 250)
    @Column(name = "description", length = 250)
    private String description;

    /**
     * JSONB - critérios para desbloquear
     */
    @Column(name = "criteria", nullable = false, length = 2000)
    private String criteria;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Achievement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Achievement code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Achievement name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public Achievement iconUrl(String iconUrl) {
        this.setIconUrl(iconUrl);
        return this;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public Achievement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCriteria() {
        return this.criteria;
    }

    public Achievement criteria(String criteria) {
        this.setCriteria(criteria);
        return this;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Achievement)) {
            return false;
        }
        return getId() != null && getId().equals(((Achievement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Achievement{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", iconUrl='" + getIconUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", criteria='" + getCriteria() + "'" +
            "}";
    }
}
