package com.bumbatech.bumbalearning.service.criteria;

import com.bumbatech.bumbalearning.domain.enumeration.DifficultyLevel;
import com.bumbatech.bumbalearning.domain.enumeration.Language;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bumbatech.bumbalearning.domain.Lesson} entity. This class is used
 * in {@link com.bumbatech.bumbalearning.web.rest.LessonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lessons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LessonCriteria implements Serializable, Criteria {

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

    private StringFilter title;

    private LanguageFilter language;

    private DifficultyLevelFilter level;

    private IntegerFilter xpReward;

    private IntegerFilter passThreshold;

    private InstantFilter createdAt;

    private StringFilter description;

    private LongFilter topicId;

    private LongFilter createdById;

    private Boolean distinct;

    public LessonCriteria() {}

    public LessonCriteria(LessonCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(LanguageFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(DifficultyLevelFilter::copy).orElse(null);
        this.xpReward = other.optionalXpReward().map(IntegerFilter::copy).orElse(null);
        this.passThreshold = other.optionalPassThreshold().map(IntegerFilter::copy).orElse(null);
        this.createdAt = other.optionalCreatedAt().map(InstantFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.topicId = other.optionalTopicId().map(LongFilter::copy).orElse(null);
        this.createdById = other.optionalCreatedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LessonCriteria copy() {
        return new LessonCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
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

    public IntegerFilter getXpReward() {
        return xpReward;
    }

    public Optional<IntegerFilter> optionalXpReward() {
        return Optional.ofNullable(xpReward);
    }

    public IntegerFilter xpReward() {
        if (xpReward == null) {
            setXpReward(new IntegerFilter());
        }
        return xpReward;
    }

    public void setXpReward(IntegerFilter xpReward) {
        this.xpReward = xpReward;
    }

    public IntegerFilter getPassThreshold() {
        return passThreshold;
    }

    public Optional<IntegerFilter> optionalPassThreshold() {
        return Optional.ofNullable(passThreshold);
    }

    public IntegerFilter passThreshold() {
        if (passThreshold == null) {
            setPassThreshold(new IntegerFilter());
        }
        return passThreshold;
    }

    public void setPassThreshold(IntegerFilter passThreshold) {
        this.passThreshold = passThreshold;
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

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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
        final LessonCriteria that = (LessonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(language, that.language) &&
            Objects.equals(level, that.level) &&
            Objects.equals(xpReward, that.xpReward) &&
            Objects.equals(passThreshold, that.passThreshold) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(description, that.description) &&
            Objects.equals(topicId, that.topicId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, language, level, xpReward, passThreshold, createdAt, description, topicId, createdById, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalXpReward().map(f -> "xpReward=" + f + ", ").orElse("") +
            optionalPassThreshold().map(f -> "passThreshold=" + f + ", ").orElse("") +
            optionalCreatedAt().map(f -> "createdAt=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalTopicId().map(f -> "topicId=" + f + ", ").orElse("") +
            optionalCreatedById().map(f -> "createdById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
