package com.bumbatech.bumbalearning.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserProfileCriteriaTest {

    @Test
    void newUserProfileCriteriaHasAllFiltersNullTest() {
        var userProfileCriteria = new UserProfileCriteria();
        assertThat(userProfileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void userProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var userProfileCriteria = new UserProfileCriteria();

        setAllFilters(userProfileCriteria);

        assertThat(userProfileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void userProfileCriteriaCopyCreatesNullFilterTest() {
        var userProfileCriteria = new UserProfileCriteria();
        var copy = userProfileCriteria.copy();

        assertThat(userProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(userProfileCriteria)
        );
    }

    @Test
    void userProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userProfileCriteria = new UserProfileCriteria();
        setAllFilters(userProfileCriteria);

        var copy = userProfileCriteria.copy();

        assertThat(userProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(userProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userProfileCriteria = new UserProfileCriteria();

        assertThat(userProfileCriteria).hasToString("UserProfileCriteria{}");
    }

    private static void setAllFilters(UserProfileCriteria userProfileCriteria) {
        userProfileCriteria.id();
        userProfileCriteria.municipalityCode();
        userProfileCriteria.currentLevel();
        userProfileCriteria.totalXp();
        userProfileCriteria.currentStreak();
        userProfileCriteria.dailyGoalXp();
        userProfileCriteria.lastActivityDate();
        userProfileCriteria.userId();
        userProfileCriteria.distinct();
    }

    private static Condition<UserProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMunicipalityCode()) &&
                condition.apply(criteria.getCurrentLevel()) &&
                condition.apply(criteria.getTotalXp()) &&
                condition.apply(criteria.getCurrentStreak()) &&
                condition.apply(criteria.getDailyGoalXp()) &&
                condition.apply(criteria.getLastActivityDate()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserProfileCriteria> copyFiltersAre(UserProfileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMunicipalityCode(), copy.getMunicipalityCode()) &&
                condition.apply(criteria.getCurrentLevel(), copy.getCurrentLevel()) &&
                condition.apply(criteria.getTotalXp(), copy.getTotalXp()) &&
                condition.apply(criteria.getCurrentStreak(), copy.getCurrentStreak()) &&
                condition.apply(criteria.getDailyGoalXp(), copy.getDailyGoalXp()) &&
                condition.apply(criteria.getLastActivityDate(), copy.getLastActivityDate()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
