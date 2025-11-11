package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.AchievementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AchievementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Achievement.class);
        Achievement achievement1 = getAchievementSample1();
        Achievement achievement2 = new Achievement();
        assertThat(achievement1).isNotEqualTo(achievement2);

        achievement2.setId(achievement1.getId());
        assertThat(achievement1).isEqualTo(achievement2);

        achievement2 = getAchievementSample2();
        assertThat(achievement1).isNotEqualTo(achievement2);
    }
}
