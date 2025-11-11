package com.bumbatech.bumbalearning.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AttemptCriteriaTest {

    @Test
    void newAttemptCriteriaHasAllFiltersNullTest() {
        var attemptCriteria = new AttemptCriteria();
        assertThat(attemptCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void attemptCriteriaFluentMethodsCreatesFiltersTest() {
        var attemptCriteria = new AttemptCriteria();

        setAllFilters(attemptCriteria);

        assertThat(attemptCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void attemptCriteriaCopyCreatesNullFilterTest() {
        var attemptCriteria = new AttemptCriteria();
        var copy = attemptCriteria.copy();

        assertThat(attemptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(attemptCriteria)
        );
    }

    @Test
    void attemptCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var attemptCriteria = new AttemptCriteria();
        setAllFilters(attemptCriteria);

        var copy = attemptCriteria.copy();

        assertThat(attemptCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(attemptCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var attemptCriteria = new AttemptCriteria();

        assertThat(attemptCriteria).hasToString("AttemptCriteria{}");
    }

    private static void setAllFilters(AttemptCriteria attemptCriteria) {
        attemptCriteria.id();
        attemptCriteria.isCorrect();
        attemptCriteria.timeSpentSeconds();
        attemptCriteria.attemptedAt();
        attemptCriteria.studentId();
        attemptCriteria.questionId();
        attemptCriteria.lessonId();
        attemptCriteria.distinct();
    }

    private static Condition<AttemptCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIsCorrect()) &&
                condition.apply(criteria.getTimeSpentSeconds()) &&
                condition.apply(criteria.getAttemptedAt()) &&
                condition.apply(criteria.getStudentId()) &&
                condition.apply(criteria.getQuestionId()) &&
                condition.apply(criteria.getLessonId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AttemptCriteria> copyFiltersAre(AttemptCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIsCorrect(), copy.getIsCorrect()) &&
                condition.apply(criteria.getTimeSpentSeconds(), copy.getTimeSpentSeconds()) &&
                condition.apply(criteria.getAttemptedAt(), copy.getAttemptedAt()) &&
                condition.apply(criteria.getStudentId(), copy.getStudentId()) &&
                condition.apply(criteria.getQuestionId(), copy.getQuestionId()) &&
                condition.apply(criteria.getLessonId(), copy.getLessonId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
