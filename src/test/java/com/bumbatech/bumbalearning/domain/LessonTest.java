package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.LessonTestSamples.*;
import static com.bumbatech.bumbalearning.domain.TopicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lesson.class);
        Lesson lesson1 = getLessonSample1();
        Lesson lesson2 = new Lesson();
        assertThat(lesson1).isNotEqualTo(lesson2);

        lesson2.setId(lesson1.getId());
        assertThat(lesson1).isEqualTo(lesson2);

        lesson2 = getLessonSample2();
        assertThat(lesson1).isNotEqualTo(lesson2);
    }

    @Test
    void topicTest() {
        Lesson lesson = getLessonRandomSampleGenerator();
        Topic topicBack = getTopicRandomSampleGenerator();

        lesson.setTopic(topicBack);
        assertThat(lesson.getTopic()).isEqualTo(topicBack);

        lesson.topic(null);
        assertThat(lesson.getTopic()).isNull();
    }
}
