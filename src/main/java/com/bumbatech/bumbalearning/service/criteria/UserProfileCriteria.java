package com.bumbatech.bumbalearning.service.criteria;

import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bumbatech.bumbalearning.domain.UserProfile} entity. This class is used
 * in {@link com.bumbatech.bumbalearning.web.rest.UserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UserLevel
     */
    public static class UserLevelFilter extends Filter<UserLevel> {

        public UserLevelFilter() {}

        public UserLevelFilter(UserLevelFilter filter) {
            super(filter);
        }

        @Override
        public UserLevelFilter copy() {
            return new UserLevelFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter municipalityCode;

    private UserLevelFilter currentLevel;

    private IntegerFilter totalXp;

    private IntegerFilter currentStreak;

    private IntegerFilter dailyGoalXp;

    private LocalDateFilter lastActivityDate;

    private LongFilter userId;

    private Boolean distinct;

    public UserProfileCriteria() {}

    public UserProfileCriteria(UserProfileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.municipalityCode = other.optionalMunicipalityCode().map(StringFilter::copy).orElse(null);
        this.currentLevel = other.optionalCurrentLevel().map(UserLevelFilter::copy).orElse(null);
        this.totalXp = other.optionalTotalXp().map(IntegerFilter::copy).orElse(null);
        this.currentStreak = other.optionalCurrentStreak().map(IntegerFilter::copy).orElse(null);
        this.dailyGoalXp = other.optionalDailyGoalXp().map(IntegerFilter::copy).orElse(null);
        this.lastActivityDate = other.optionalLastActivityDate().map(LocalDateFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserProfileCriteria copy() {
        return new UserProfileCriteria(this);
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

    public StringFilter getMunicipalityCode() {
        return municipalityCode;
    }

    public Optional<StringFilter> optionalMunicipalityCode() {
        return Optional.ofNullable(municipalityCode);
    }

    public StringFilter municipalityCode() {
        if (municipalityCode == null) {
            setMunicipalityCode(new StringFilter());
        }
        return municipalityCode;
    }

    public void setMunicipalityCode(StringFilter municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public UserLevelFilter getCurrentLevel() {
        return currentLevel;
    }

    public Optional<UserLevelFilter> optionalCurrentLevel() {
        return Optional.ofNullable(currentLevel);
    }

    public UserLevelFilter currentLevel() {
        if (currentLevel == null) {
            setCurrentLevel(new UserLevelFilter());
        }
        return currentLevel;
    }

    public void setCurrentLevel(UserLevelFilter currentLevel) {
        this.currentLevel = currentLevel;
    }

    public IntegerFilter getTotalXp() {
        return totalXp;
    }

    public Optional<IntegerFilter> optionalTotalXp() {
        return Optional.ofNullable(totalXp);
    }

    public IntegerFilter totalXp() {
        if (totalXp == null) {
            setTotalXp(new IntegerFilter());
        }
        return totalXp;
    }

    public void setTotalXp(IntegerFilter totalXp) {
        this.totalXp = totalXp;
    }

    public IntegerFilter getCurrentStreak() {
        return currentStreak;
    }

    public Optional<IntegerFilter> optionalCurrentStreak() {
        return Optional.ofNullable(currentStreak);
    }

    public IntegerFilter currentStreak() {
        if (currentStreak == null) {
            setCurrentStreak(new IntegerFilter());
        }
        return currentStreak;
    }

    public void setCurrentStreak(IntegerFilter currentStreak) {
        this.currentStreak = currentStreak;
    }

    public IntegerFilter getDailyGoalXp() {
        return dailyGoalXp;
    }

    public Optional<IntegerFilter> optionalDailyGoalXp() {
        return Optional.ofNullable(dailyGoalXp);
    }

    public IntegerFilter dailyGoalXp() {
        if (dailyGoalXp == null) {
            setDailyGoalXp(new IntegerFilter());
        }
        return dailyGoalXp;
    }

    public void setDailyGoalXp(IntegerFilter dailyGoalXp) {
        this.dailyGoalXp = dailyGoalXp;
    }

    public LocalDateFilter getLastActivityDate() {
        return lastActivityDate;
    }

    public Optional<LocalDateFilter> optionalLastActivityDate() {
        return Optional.ofNullable(lastActivityDate);
    }

    public LocalDateFilter lastActivityDate() {
        if (lastActivityDate == null) {
            setLastActivityDate(new LocalDateFilter());
        }
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDateFilter lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final UserProfileCriteria that = (UserProfileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(municipalityCode, that.municipalityCode) &&
            Objects.equals(currentLevel, that.currentLevel) &&
            Objects.equals(totalXp, that.totalXp) &&
            Objects.equals(currentStreak, that.currentStreak) &&
            Objects.equals(dailyGoalXp, that.dailyGoalXp) &&
            Objects.equals(lastActivityDate, that.lastActivityDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, municipalityCode, currentLevel, totalXp, currentStreak, dailyGoalXp, lastActivityDate, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMunicipalityCode().map(f -> "municipalityCode=" + f + ", ").orElse("") +
            optionalCurrentLevel().map(f -> "currentLevel=" + f + ", ").orElse("") +
            optionalTotalXp().map(f -> "totalXp=" + f + ", ").orElse("") +
            optionalCurrentStreak().map(f -> "currentStreak=" + f + ", ").orElse("") +
            optionalDailyGoalXp().map(f -> "dailyGoalXp=" + f + ", ").orElse("") +
            optionalLastActivityDate().map(f -> "lastActivityDate=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
