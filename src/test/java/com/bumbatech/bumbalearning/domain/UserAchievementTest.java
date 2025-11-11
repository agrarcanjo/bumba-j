package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.AchievementTestSamples.*;
import static com.bumbatech.bumbalearning.domain.UserAchievementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAchievementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAchievement.class);
        UserAchievement userAchievement1 = getUserAchievementSample1();
        UserAchievement userAchievement2 = new UserAchievement();
        assertThat(userAchievement1).isNotEqualTo(userAchievement2);

        userAchievement2.setId(userAchievement1.getId());
        assertThat(userAchievement1).isEqualTo(userAchievement2);

        userAchievement2 = getUserAchievementSample2();
        assertThat(userAchievement1).isNotEqualTo(userAchievement2);
    }

    @Test
    void achievementTest() {
        UserAchievement userAchievement = getUserAchievementRandomSampleGenerator();
        Achievement achievementBack = getAchievementRandomSampleGenerator();

        userAchievement.setAchievement(achievementBack);
        assertThat(userAchievement.getAchievement()).isEqualTo(achievementBack);

        userAchievement.achievement(null);
        assertThat(userAchievement.getAchievement()).isNull();
    }
}
