package com.bumbatech.bumbalearning.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bumbatech.bumbalearning.domain.Attempt} entity. This class is used
 * in {@link com.bumbatech.bumbalearning.web.rest.AttemptResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attempts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttemptCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter isCorrect;

    private IntegerFilter timeSpentSeconds;

    private InstantFilter attemptedAt;

    private LongFilter studentId;

    private LongFilter questionId;

    private LongFilter lessonId;

    private Boolean distinct;

    public AttemptCriteria() {}

    public AttemptCriteria(AttemptCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.isCorrect = other.optionalIsCorrect().map(BooleanFilter::copy).orElse(null);
        this.timeSpentSeconds = other.optionalTimeSpentSeconds().map(IntegerFilter::copy).orElse(null);
        this.attemptedAt = other.optionalAttemptedAt().map(InstantFilter::copy).orElse(null);
        this.studentId = other.optionalStudentId().map(LongFilter::copy).orElse(null);
        this.questionId = other.optionalQuestionId().map(LongFilter::copy).orElse(null);
        this.lessonId = other.optionalLessonId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AttemptCriteria copy() {
        return new AttemptCriteria(this);
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

    public BooleanFilter getIsCorrect() {
        return isCorrect;
    }

    public Optional<BooleanFilter> optionalIsCorrect() {
        return Optional.ofNullable(isCorrect);
    }

    public BooleanFilter isCorrect() {
        if (isCorrect == null) {
            setIsCorrect(new BooleanFilter());
        }
        return isCorrect;
    }

    public void setIsCorrect(BooleanFilter isCorrect) {
        this.isCorrect = isCorrect;
    }

    public IntegerFilter getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public Optional<IntegerFilter> optionalTimeSpentSeconds() {
        return Optional.ofNullable(timeSpentSeconds);
    }

    public IntegerFilter timeSpentSeconds() {
        if (timeSpentSeconds == null) {
            setTimeSpentSeconds(new IntegerFilter());
        }
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(IntegerFilter timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public InstantFilter getAttemptedAt() {
        return attemptedAt;
    }

    public Optional<InstantFilter> optionalAttemptedAt() {
        return Optional.ofNullable(attemptedAt);
    }

    public InstantFilter attemptedAt() {
        if (attemptedAt == null) {
            setAttemptedAt(new InstantFilter());
        }
        return attemptedAt;
    }

    public void setAttemptedAt(InstantFilter attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    public LongFilter getStudentId() {
        return studentId;
    }

    public Optional<LongFilter> optionalStudentId() {
        return Optional.ofNullable(studentId);
    }

    public LongFilter studentId() {
        if (studentId == null) {
            setStudentId(new LongFilter());
        }
        return studentId;
    }

    public void setStudentId(LongFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public Optional<LongFilter> optionalQuestionId() {
        return Optional.ofNullable(questionId);
    }

    public LongFilter questionId() {
        if (questionId == null) {
            setQuestionId(new LongFilter());
        }
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
    }

    public LongFilter getLessonId() {
        return lessonId;
    }

    public Optional<LongFilter> optionalLessonId() {
        return Optional.ofNullable(lessonId);
    }

    public LongFilter lessonId() {
        if (lessonId == null) {
            setLessonId(new LongFilter());
        }
        return lessonId;
    }

    public void setLessonId(LongFilter lessonId) {
        this.lessonId = lessonId;
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
        final AttemptCriteria that = (AttemptCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(isCorrect, that.isCorrect) &&
            Objects.equals(timeSpentSeconds, that.timeSpentSeconds) &&
            Objects.equals(attemptedAt, that.attemptedAt) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(lessonId, that.lessonId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isCorrect, timeSpentSeconds, attemptedAt, studentId, questionId, lessonId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttemptCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIsCorrect().map(f -> "isCorrect=" + f + ", ").orElse("") +
            optionalTimeSpentSeconds().map(f -> "timeSpentSeconds=" + f + ", ").orElse("") +
            optionalAttemptedAt().map(f -> "attemptedAt=" + f + ", ").orElse("") +
            optionalStudentId().map(f -> "studentId=" + f + ", ").orElse("") +
            optionalQuestionId().map(f -> "questionId=" + f + ", ").orElse("") +
            optionalLessonId().map(f -> "lessonId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
