package com.bumbatech.bumbalearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonQuestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonQuestionDTO.class);
        LessonQuestionDTO lessonQuestionDTO1 = new LessonQuestionDTO();
        lessonQuestionDTO1.setId(1L);
        LessonQuestionDTO lessonQuestionDTO2 = new LessonQuestionDTO();
        assertThat(lessonQuestionDTO1).isNotEqualTo(lessonQuestionDTO2);
        lessonQuestionDTO2.setId(lessonQuestionDTO1.getId());
        assertThat(lessonQuestionDTO1).isEqualTo(lessonQuestionDTO2);
        lessonQuestionDTO2.setId(2L);
        assertThat(lessonQuestionDTO1).isNotEqualTo(lessonQuestionDTO2);
        lessonQuestionDTO1.setId(null);
        assertThat(lessonQuestionDTO1).isNotEqualTo(lessonQuestionDTO2);
    }
}
