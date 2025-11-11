package com.bumbatech.bumbalearning.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LessonCriteriaTest {

    @Test
    void newLessonCriteriaHasAllFiltersNullTest() {
        var lessonCriteria = new LessonCriteria();
        assertThat(lessonCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void lessonCriteriaFluentMethodsCreatesFiltersTest() {
        var lessonCriteria = new LessonCriteria();

        setAllFilters(lessonCriteria);

        assertThat(lessonCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void lessonCriteriaCopyCreatesNullFilterTest() {
        var lessonCriteria = new LessonCriteria();
        var copy = lessonCriteria.copy();

        assertThat(lessonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(lessonCriteria)
        );
    }

    @Test
    void lessonCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var lessonCriteria = new LessonCriteria();
        setAllFilters(lessonCriteria);

        var copy = lessonCriteria.copy();

        assertThat(lessonCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(lessonCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var lessonCriteria = new LessonCriteria();

        assertThat(lessonCriteria).hasToString("LessonCriteria{}");
    }

    private static void setAllFilters(LessonCriteria lessonCriteria) {
        lessonCriteria.id();
        lessonCriteria.title();
        lessonCriteria.language();
        lessonCriteria.level();
        lessonCriteria.xpReward();
        lessonCriteria.passThreshold();
        lessonCriteria.createdAt();
        lessonCriteria.description();
        lessonCriteria.topicId();
        lessonCriteria.createdById();
        lessonCriteria.distinct();
    }

    private static Condition<LessonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getLevel()) &&
                condition.apply(criteria.getXpReward()) &&
                condition.apply(criteria.getPassThreshold()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTopicId()) &&
                condition.apply(criteria.getCreatedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LessonCriteria> copyFiltersAre(LessonCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getLevel(), copy.getLevel()) &&
                condition.apply(criteria.getXpReward(), copy.getXpReward()) &&
                condition.apply(criteria.getPassThreshold(), copy.getPassThreshold()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTopicId(), copy.getTopicId()) &&
                condition.apply(criteria.getCreatedById(), copy.getCreatedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
