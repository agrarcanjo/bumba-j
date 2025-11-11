package com.bumbatech.bumbalearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LessonAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LessonAssignmentDTO.class);
        LessonAssignmentDTO lessonAssignmentDTO1 = new LessonAssignmentDTO();
        lessonAssignmentDTO1.setId(1L);
        LessonAssignmentDTO lessonAssignmentDTO2 = new LessonAssignmentDTO();
        assertThat(lessonAssignmentDTO1).isNotEqualTo(lessonAssignmentDTO2);
        lessonAssignmentDTO2.setId(lessonAssignmentDTO1.getId());
        assertThat(lessonAssignmentDTO1).isEqualTo(lessonAssignmentDTO2);
        lessonAssignmentDTO2.setId(2L);
        assertThat(lessonAssignmentDTO1).isNotEqualTo(lessonAssignmentDTO2);
        lessonAssignmentDTO1.setId(null);
        assertThat(lessonAssignmentDTO1).isNotEqualTo(lessonAssignmentDTO2);
    }
}
