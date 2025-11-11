package com.bumbatech.bumbalearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentLessonProgressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentLessonProgressDTO.class);
        StudentLessonProgressDTO studentLessonProgressDTO1 = new StudentLessonProgressDTO();
        studentLessonProgressDTO1.setId(1L);
        StudentLessonProgressDTO studentLessonProgressDTO2 = new StudentLessonProgressDTO();
        assertThat(studentLessonProgressDTO1).isNotEqualTo(studentLessonProgressDTO2);
        studentLessonProgressDTO2.setId(studentLessonProgressDTO1.getId());
        assertThat(studentLessonProgressDTO1).isEqualTo(studentLessonProgressDTO2);
        studentLessonProgressDTO2.setId(2L);
        assertThat(studentLessonProgressDTO1).isNotEqualTo(studentLessonProgressDTO2);
        studentLessonProgressDTO1.setId(null);
        assertThat(studentLessonProgressDTO1).isNotEqualTo(studentLessonProgressDTO2);
    }
}
