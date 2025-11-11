package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.LessonQuestionTestSamples.*;
import static com.bumbatech.bumbalearning.domain.LessonTestSamples.*;
import static com.bumbatech.bumbalearning.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonQuestion.class);
        LessonQuestion lessonQuestion1 = getLessonQuestionSample1();
        LessonQuestion lessonQuestion2 = new LessonQuestion();
        assertThat(lessonQuestion1).isNotEqualTo(lessonQuestion2);

        lessonQuestion2.setId(lessonQuestion1.getId());
        assertThat(lessonQuestion1).isEqualTo(lessonQuestion2);

        lessonQuestion2 = getLessonQuestionSample2();
        assertThat(lessonQuestion1).isNotEqualTo(lessonQuestion2);
    }

    @Test
    void lessonTest() {
        LessonQuestion lessonQuestion = getLessonQuestionRandomSampleGenerator();
        Lesson lessonBack = getLessonRandomSampleGenerator();

        lessonQuestion.setLesson(lessonBack);
        assertThat(lessonQuestion.getLesson()).isEqualTo(lessonBack);

        lessonQuestion.lesson(null);
        assertThat(lessonQuestion.getLesson()).isNull();
    }

    @Test
    void questionTest() {
        LessonQuestion lessonQuestion = getLessonQuestionRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        lessonQuestion.setQuestion(questionBack);
        assertThat(lessonQuestion.getQuestion()).isEqualTo(questionBack);

        lessonQuestion.question(null);
        assertThat(lessonQuestion.getQuestion()).isNull();
    }
}
