package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.LessonTestSamples.*;
import static com.bumbatech.bumbalearning.domain.RecommendationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecommendationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recommendation.class);
        Recommendation recommendation1 = getRecommendationSample1();
        Recommendation recommendation2 = new Recommendation();
        assertThat(recommendation1).isNotEqualTo(recommendation2);

        recommendation2.setId(recommendation1.getId());
        assertThat(recommendation1).isEqualTo(recommendation2);

        recommendation2 = getRecommendationSample2();
        assertThat(recommendation1).isNotEqualTo(recommendation2);
    }

    @Test
    void lessonTest() {
        Recommendation recommendation = getRecommendationRandomSampleGenerator();
        Lesson lessonBack = getLessonRandomSampleGenerator();

        recommendation.setLesson(lessonBack);
        assertThat(recommendation.getLesson()).isEqualTo(lessonBack);

        recommendation.lesson(null);
        assertThat(recommendation.getLesson()).isNull();
    }
}
