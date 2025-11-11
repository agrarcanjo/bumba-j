package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.AttemptTestSamples.*;
import static com.bumbatech.bumbalearning.domain.LessonTestSamples.*;
import static com.bumbatech.bumbalearning.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttemptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attempt.class);
        Attempt attempt1 = getAttemptSample1();
        Attempt attempt2 = new Attempt();
        assertThat(attempt1).isNotEqualTo(attempt2);

        attempt2.setId(attempt1.getId());
        assertThat(attempt1).isEqualTo(attempt2);

        attempt2 = getAttemptSample2();
        assertThat(attempt1).isNotEqualTo(attempt2);
    }

    @Test
    void questionTest() {
        Attempt attempt = getAttemptRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        attempt.setQuestion(questionBack);
        assertThat(attempt.getQuestion()).isEqualTo(questionBack);

        attempt.question(null);
        assertThat(attempt.getQuestion()).isNull();
    }

    @Test
    void lessonTest() {
        Attempt attempt = getAttemptRandomSampleGenerator();
        Lesson lessonBack = getLessonRandomSampleGenerator();

        attempt.setLesson(lessonBack);
        assertThat(attempt.getLesson()).isEqualTo(lessonBack);

        attempt.lesson(null);
        assertThat(attempt.getLesson()).isNull();
    }
}
