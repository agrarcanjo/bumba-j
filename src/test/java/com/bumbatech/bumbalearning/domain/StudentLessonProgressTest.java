package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.LessonAssignmentTestSamples.*;
import static com.bumbatech.bumbalearning.domain.LessonTestSamples.*;
import static com.bumbatech.bumbalearning.domain.StudentLessonProgressTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentLessonProgressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentLessonProgress.class);
        StudentLessonProgress studentLessonProgress1 = getStudentLessonProgressSample1();
        StudentLessonProgress studentLessonProgress2 = new StudentLessonProgress();
        assertThat(studentLessonProgress1).isNotEqualTo(studentLessonProgress2);

        studentLessonProgress2.setId(studentLessonProgress1.getId());
        assertThat(studentLessonProgress1).isEqualTo(studentLessonProgress2);

        studentLessonProgress2 = getStudentLessonProgressSample2();
        assertThat(studentLessonProgress1).isNotEqualTo(studentLessonProgress2);
    }

    @Test
    void lessonTest() {
        StudentLessonProgress studentLessonProgress = getStudentLessonProgressRandomSampleGenerator();
        Lesson lessonBack = getLessonRandomSampleGenerator();

        studentLessonProgress.setLesson(lessonBack);
        assertThat(studentLessonProgress.getLesson()).isEqualTo(lessonBack);

        studentLessonProgress.lesson(null);
        assertThat(studentLessonProgress.getLesson()).isNull();
    }

    @Test
    void assignmentTest() {
        StudentLessonProgress studentLessonProgress = getStudentLessonProgressRandomSampleGenerator();
        LessonAssignment lessonAssignmentBack = getLessonAssignmentRandomSampleGenerator();

        studentLessonProgress.setAssignment(lessonAssignmentBack);
        assertThat(studentLessonProgress.getAssignment()).isEqualTo(lessonAssignmentBack);

        studentLessonProgress.assignment(null);
        assertThat(studentLessonProgress.getAssignment()).isNull();
    }
}
