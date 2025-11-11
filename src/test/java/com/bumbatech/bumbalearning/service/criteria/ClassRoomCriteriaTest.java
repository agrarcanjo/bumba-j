package com.bumbatech.bumbalearning.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClassRoomCriteriaTest {

    @Test
    void newClassRoomCriteriaHasAllFiltersNullTest() {
        var classRoomCriteria = new ClassRoomCriteria();
        assertThat(classRoomCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void classRoomCriteriaFluentMethodsCreatesFiltersTest() {
        var classRoomCriteria = new ClassRoomCriteria();

        setAllFilters(classRoomCriteria);

        assertThat(classRoomCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void classRoomCriteriaCopyCreatesNullFilterTest() {
        var classRoomCriteria = new ClassRoomCriteria();
        var copy = classRoomCriteria.copy();

        assertThat(classRoomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(classRoomCriteria)
        );
    }

    @Test
    void classRoomCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var classRoomCriteria = new ClassRoomCriteria();
        setAllFilters(classRoomCriteria);

        var copy = classRoomCriteria.copy();

        assertThat(classRoomCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(classRoomCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var classRoomCriteria = new ClassRoomCriteria();

        assertThat(classRoomCriteria).hasToString("ClassRoomCriteria{}");
    }

    private static void setAllFilters(ClassRoomCriteria classRoomCriteria) {
        classRoomCriteria.id();
        classRoomCriteria.name();
        classRoomCriteria.language();
        classRoomCriteria.createdAt();
        classRoomCriteria.description();
        classRoomCriteria.teacherId();
        classRoomCriteria.distinct();
    }

    private static Condition<ClassRoomCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTeacherId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClassRoomCriteria> copyFiltersAre(ClassRoomCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTeacherId(), copy.getTeacherId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
