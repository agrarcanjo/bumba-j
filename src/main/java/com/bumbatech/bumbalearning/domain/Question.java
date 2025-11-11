package com.bumbatech.bumbalearning.domain;

import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import com.bumbatech.bumbalearning.domain.enumeration.QuestionType;
import com.bumbatech.bumbalearning.domain.enumeration.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Question - Banco de questões
 * Armazena todos os tipos de atividades
 */
@Entity
@Table(name = "question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Tipo da questão
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestionType type;

    /**
     * Idioma da questão
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    /**
     * Habilidade trabalhada
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "skill", nullable = false)
    private Skill skill;

    /**
     * Nível de dificuldade
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private DifficultyLevel level;

    /**
     * Data de criação
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Enunciado da questão
     */
    @NotNull
    @Size(max = 2000)
    @Column(name = "prompt", length = 2000, nullable = false)
    private String prompt;

    /**
     * JSONB - estrutura específica por tipo
     */
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * JSONB - estrutura específica por tipo
     */
    @Lob
    @Column(name = "assets")
    private String assets;

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

    public Question id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionType getType() {
        return this.type;
    }

    public Question type(QuestionType type) {
        this.setType(type);
        return this;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Question language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public Question skill(Skill skill) {
        this.setSkill(skill);
        return this;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public DifficultyLevel getLevel() {
        return this.level;
    }

    public Question level(DifficultyLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Question createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public Question prompt(String prompt) {
        this.setPrompt(prompt);
        return this;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getContent() {
        return this.content;
    }

    public Question content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAssets() {
        return this.assets;
    }

    public Question assets(String assets) {
        this.setAssets(assets);
        return this;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Question topic(Topic topic) {
        this.setTopic(topic);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Question createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", language='" + getLanguage() + "'" +
            ", skill='" + getSkill() + "'" +
            ", level='" + getLevel() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", prompt='" + getPrompt() + "'" +
            ", content='" + getContent() + "'" +
            ", assets='" + getAssets() + "'" +
            "}";
    }
}
