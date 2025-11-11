package com.bumbatech.bumbalearning.service.criteria;

import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import com.bumbatech.bumbalearning.domain.enumeration.QuestionType;
import com.bumbatech.bumbalearning.domain.enumeration.Skill;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bumbatech.bumbalearning.domain.Question} entity. This class is used
 * in {@link com.bumbatech.bumbalearning.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuestionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering QuestionType
     */
    public static class QuestionTypeFilter extends Filter<QuestionType> {

        public QuestionTypeFilter() {}

        public QuestionTypeFilter(QuestionTypeFilter filter) {
            super(filter);
        }

        @Override
        public QuestionTypeFilter copy() {
            return new QuestionTypeFilter(this);
        }
    }

    /**
     * Class for filtering Language
     */
    public static class LanguageFilter extends Filter<Language> {

        public LanguageFilter() {}

        public LanguageFilter(LanguageFilter filter) {
            super(filter);
        }

        @Override
        public LanguageFilter copy() {
            return new LanguageFilter(this);
        }
    }

    /**
     * Class for filtering Skill
     */
    public static class SkillFilter extends Filter<Skill> {

        public SkillFilter() {}

        public SkillFilter(SkillFilter filter) {
            super(filter);
        }

        @Override
        public SkillFilter copy() {
            return new SkillFilter(this);
        }
    }

    /**
     * Class for filtering DifficultyLevel
     */
    public static class DifficultyLevelFilter extends Filter<DifficultyLevel> {

        public DifficultyLevelFilter() {}

        public DifficultyLevelFilter(DifficultyLevelFilter filter) {
            super(filter);
        }

        @Override
        public DifficultyLevelFilter copy() {
            return new DifficultyLevelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private QuestionTypeFilter type;

    private LanguageFilter language;

    private SkillFilter skill;

    private DifficultyLevelFilter level;

    private InstantFilter createdAt;

    private StringFilter prompt;

    private LongFilter topicId;

    private LongFilter createdById;

    private Boolean distinct;

    public QuestionCriteria() {}

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(QuestionTypeFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(LanguageFilter::copy).orElse(null);
        this.skill = other.optionalSkill().map(SkillFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(DifficultyLevelFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.prompt = other.optionalPrompt().map(StringFilter::copy).orElse(null);
        this.topicId = other.optionalTopicId().map(LongFilter::copy).orElse(null);
        this.createdById = other.optionalCreatedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public QuestionTypeFilter getType() {
        return type;
    }

    public Optional<QuestionTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public QuestionTypeFilter type() {
        if (type == null) {
            setType(new QuestionTypeFilter());
        }
        return type;
    }

    public void setType(QuestionTypeFilter type) {
        this.type = type;
    }

    public LanguageFilter getLanguage() {
        return language;
    }

    public Optional<LanguageFilter> optionalLanguage() {
        return Optional.ofNullable(language);
    }

    public LanguageFilter language() {
        if (language == null) {
            setLanguage(new LanguageFilter());
        }
        return language;
    }

    public void setLanguage(LanguageFilter language) {
        this.language = language;
    }

    public SkillFilter getSkill() {
        return skill;
    }

    public Optional<SkillFilter> optionalSkill() {
        return Optional.ofNullable(skill);
    }

    public SkillFilter skill() {
        if (skill == null) {
            setSkill(new SkillFilter());
        }
        return skill;
    }

    public void setSkill(SkillFilter skill) {
        this.skill = skill;
    }

    public DifficultyLevelFilter getLevel() {
        return level;
    }

    public Optional<DifficultyLevelFilter> optionalLevel() {
        return Optional.ofNullable(level);
    }

    public DifficultyLevelFilter level() {
        if (level == null) {
            setLevel(new DifficultyLevelFilter());
        }
        return level;
    }

    public void setLevel(DifficultyLevelFilter level) {
        this.level = level;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public Optional<InstantFilter> optionalCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            setCreatedAt(new InstantFilter());
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getPrompt() {
        return prompt;
    }

    public Optional<StringFilter> optionalPrompt() {
        return Optional.ofNullable(prompt);
    }

    public StringFilter prompt() {
        if (prompt == null) {
            setPrompt(new StringFilter());
        }
        return prompt;
    }

    public void setPrompt(StringFilter prompt) {
        this.prompt = prompt;
    }

    public LongFilter getTopicId() {
        return topicId;
    }

    public Optional<LongFilter> optionalTopicId() {
        return Optional.ofNullable(topicId);
    }

    public LongFilter topicId() {
        if (topicId == null) {
            setTopicId(new LongFilter());
        }
        return topicId;
    }

    public void setTopicId(LongFilter topicId) {
        this.topicId = topicId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public Optional<LongFilter> optionalCreatedById() {
        return Optional.ofNullable(createdById);
    }

    public LongFilter createdById() {
        if (createdById == null) {
            setCreatedById(new LongFilter());
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestionCriteria that = (QuestionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(language, that.language) &&
            Objects.equals(skill, that.skill) &&
            Objects.equals(level, that.level) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(prompt, that.prompt) &&
            Objects.equals(topicId, that.topicId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, language, skill, level, createdAt, prompt, topicId, createdById, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalSkill().map(f -> "skill=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalPrompt().map(f -> "prompt=" + f + ", ").orElse("") +
            optionalTopicId().map(f -> "topicId=" + f + ", ").orElse("") +
            optionalCreatedById().map(f -> "createdById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
