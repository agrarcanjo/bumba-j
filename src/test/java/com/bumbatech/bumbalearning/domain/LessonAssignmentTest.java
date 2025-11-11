package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.ClassRoomTestSamples.*;
import static com.bumbatech.bumbalearning.domain.LessonAssignmentTestSamples.*;
import static com.bumbatech.bumbalearning.domain.LessonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonAssignment.class);
        LessonAssignment lessonAssignment1 = getLessonAssignmentSample1();
        LessonAssignment lessonAssignment2 = new LessonAssignment();
        assertThat(lessonAssignment1).isNotEqualTo(lessonAssignment2);

        lessonAssignment2.setId(lessonAssignment1.getId());
        assertThat(lessonAssignment1).isEqualTo(lessonAssignment2);

        lessonAssignment2 = getLessonAssignmentSample2();
        assertThat(lessonAssignment1).isNotEqualTo(lessonAssignment2);
    }

    @Test
    void classRoomTest() {
        LessonAssignment lessonAssignment = getLessonAssignmentRandomSampleGenerator();
        ClassRoom classRoomBack = getClassRoomRandomSampleGenerator();

        lessonAssignment.setClassRoom(classRoomBack);
        assertThat(lessonAssignment.getClassRoom()).isEqualTo(classRoomBack);

        lessonAssignment.classRoom(null);
        assertThat(lessonAssignment.getClassRoom()).isNull();
    }

    @Test
    void lessonTest() {
        LessonAssignment lessonAssignment = getLessonAssignmentRandomSampleGenerator();
        Lesson lessonBack = getLessonRandomSampleGenerator();

        lessonAssignment.setLesson(lessonBack);
        assertThat(lessonAssignment.getLesson()).isEqualTo(lessonBack);

        lessonAssignment.lesson(null);
        assertThat(lessonAssignment.getLesson()).isNull();
    }
}
