package com.bumbatech.bumbalearning.service.criteria;

import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bumbatech.bumbalearning.domain.StudentLessonProgress} entity. This class is used
 * in {@link com.bumbatech.bumbalearning.web.rest.StudentLessonProgressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /student-lesson-progresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentLessonProgressCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LessonStatus
     */
    public static class LessonStatusFilter extends Filter<LessonStatus> {

        public LessonStatusFilter() {}

        public LessonStatusFilter(LessonStatusFilter filter) {
            super(filter);
        }

        @Override
        public LessonStatusFilter copy() {
            return new LessonStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LessonStatusFilter status;

    private IntegerFilter score;

    private IntegerFilter xpEarned;

    private InstantFilter completedAt;

    private BooleanFilter isLate;

    private LongFilter studentId;

    private LongFilter lessonId;

    private LongFilter assignmentId;

    private Boolean distinct;

    public StudentLessonProgressCriteria() {}

    public StudentLessonProgressCriteria(StudentLessonProgressCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(LessonStatusFilter::copy).orElse(null);
        this.score = other.optionalScore().map(IntegerFilter::copy).orElse(null);
        this.xpEarned = other.optionalXpEarned().map(IntegerFilter::copy).orElse(null);
        this.completedAt = other.optionalCompletedAt().map(InstantFilter::copy).orElse(null);
        this.isLate = other.optionalIsLate().map(BooleanFilter::copy).orElse(null);
        this.studentId = other.optionalStudentId().map(LongFilter::copy).orElse(null);
        this.lessonId = other.optionalLessonId().map(LongFilter::copy).orElse(null);
        this.assignmentId = other.optionalAssignmentId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public StudentLessonProgressCriteria copy() {
        return new StudentLessonProgressCriteria(this);
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

    public LessonStatusFilter getStatus() {
        return status;
    }

    public Optional<LessonStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public LessonStatusFilter status() {
        if (status == null) {
            setStatus(new LessonStatusFilter());
        }
        return status;
    }

    public void setStatus(LessonStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getScore() {
        return score;
    }

    public Optional<IntegerFilter> optionalScore() {
        return Optional.ofNullable(score);
    }

    public IntegerFilter score() {
        if (score == null) {
            setScore(new IntegerFilter());
        }
        return score;
    }

    public void setScore(IntegerFilter score) {
        this.score = score;
    }

    public IntegerFilter getXpEarned() {
        return xpEarned;
    }

    public Optional<IntegerFilter> optionalXpEarned() {
        return Optional.ofNullable(xpEarned);
    }

    public IntegerFilter xpEarned() {
        if (xpEarned == null) {
            setXpEarned(new IntegerFilter());
        }
        return xpEarned;
    }

    public void setXpEarned(IntegerFilter xpEarned) {
        this.xpEarned = xpEarned;
    }

    public InstantFilter getCompletedAt() {
        return completedAt;
    }

    public Optional<InstantFilter> optionalCompletedAt() {
        return Optional.ofNullable(completedAt);
    }

    public InstantFilter completedAt() {
        if (completedAt == null) {
            setCompletedAt(new InstantFilter());
        }
        return completedAt;
    }

    public void setCompletedAt(InstantFilter completedAt) {
        this.completedAt = completedAt;
    }

    public BooleanFilter getIsLate() {
        return isLate;
    }

    public Optional<BooleanFilter> optionalIsLate() {
        return Optional.ofNullable(isLate);
    }

    public BooleanFilter isLate() {
        if (isLate == null) {
            setIsLate(new BooleanFilter());
        }
        return isLate;
    }

    public void setIsLate(BooleanFilter isLate) {
        this.isLate = isLate;
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

    public LongFilter getAssignmentId() {
        return assignmentId;
    }

    public Optional<LongFilter> optionalAssignmentId() {
        return Optional.ofNullable(assignmentId);
    }

    public LongFilter assignmentId() {
        if (assignmentId == null) {
            setAssignmentId(new LongFilter());
        }
        return assignmentId;
    }

    public void setAssignmentId(LongFilter assignmentId) {
        this.assignmentId = assignmentId;
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
        final StudentLessonProgressCriteria that = (StudentLessonProgressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(score, that.score) &&
            Objects.equals(xpEarned, that.xpEarned) &&
            Objects.equals(completedAt, that.completedAt) &&
            Objects.equals(isLate, that.isLate) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(lessonId, that.lessonId) &&
            Objects.equals(assignmentId, that.assignmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, score, xpEarned, completedAt, isLate, studentId, lessonId, assignmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentLessonProgressCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalScore().map(f -> "score=" + f + ", ").orElse("") +
            optionalXpEarned().map(f -> "xpEarned=" + f + ", ").orElse("") +
            optionalCompletedAt().map(f -> "completedAt=" + f + ", ").orElse("") +
            optionalIsLate().map(f -> "isLate=" + f + ", ").orElse("") +
            optionalStudentId().map(f -> "studentId=" + f + ", ").orElse("") +
            optionalLessonId().map(f -> "lessonId=" + f + ", ").orElse("") +
            optionalAssignmentId().map(f -> "assignmentId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
