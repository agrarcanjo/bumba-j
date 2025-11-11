package com.bumbatech.bumbalearning.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StudentLessonProgressCriteriaTest {

    @Test
    void newStudentLessonProgressCriteriaHasAllFiltersNullTest() {
        var studentLessonProgressCriteria = new StudentLessonProgressCriteria();
        assertThat(studentLessonProgressCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void studentLessonProgressCriteriaFluentMethodsCreatesFiltersTest() {
        var studentLessonProgressCriteria = new StudentLessonProgressCriteria();

        setAllFilters(studentLessonProgressCriteria);

        assertThat(studentLessonProgressCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void studentLessonProgressCriteriaCopyCreatesNullFilterTest() {
        var studentLessonProgressCriteria = new StudentLessonProgressCriteria();
        var copy = studentLessonProgressCriteria.copy();

        assertThat(studentLessonProgressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(studentLessonProgressCriteria)
        );
    }

    @Test
    void studentLessonProgressCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var studentLessonProgressCriteria = new StudentLessonProgressCriteria();
        setAllFilters(studentLessonProgressCriteria);

        var copy = studentLessonProgressCriteria.copy();

        assertThat(studentLessonProgressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(studentLessonProgressCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var studentLessonProgressCriteria = new StudentLessonProgressCriteria();

        assertThat(studentLessonProgressCriteria).hasToString("StudentLessonProgressCriteria{}");
    }

    private static void setAllFilters(StudentLessonProgressCriteria studentLessonProgressCriteria) {
        studentLessonProgressCriteria.id();
        studentLessonProgressCriteria.status();
        studentLessonProgressCriteria.score();
        studentLessonProgressCriteria.xpEarned();
        studentLessonProgressCriteria.completedAt();
        studentLessonProgressCriteria.isLate();
        studentLessonProgressCriteria.studentId();
        studentLessonProgressCriteria.lessonId();
        studentLessonProgressCriteria.assignmentId();
        studentLessonProgressCriteria.distinct();
    }

    private static Condition<StudentLessonProgressCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getScore()) &&
                condition.apply(criteria.getXpEarned()) &&
                condition.apply(criteria.getCompletedAt()) &&
                condition.apply(criteria.getIsLate()) &&
                condition.apply(criteria.getStudentId()) &&
                condition.apply(criteria.getLessonId()) &&
                condition.apply(criteria.getAssignmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StudentLessonProgressCriteria> copyFiltersAre(
        StudentLessonProgressCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getScore(), copy.getScore()) &&
                condition.apply(criteria.getXpEarned(), copy.getXpEarned()) &&
                condition.apply(criteria.getCompletedAt(), copy.getCompletedAt()) &&
                condition.apply(criteria.getIsLate(), copy.getIsLate()) &&
                condition.apply(criteria.getStudentId(), copy.getStudentId()) &&
                condition.apply(criteria.getLessonId(), copy.getLessonId()) &&
                condition.apply(criteria.getAssignmentId(), copy.getAssignmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
