package com.bumbatech.bumbalearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassRoomDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassRoomDTO.class);
        ClassRoomDTO classRoomDTO1 = new ClassRoomDTO();
        classRoomDTO1.setId(1L);
        ClassRoomDTO classRoomDTO2 = new ClassRoomDTO();
        assertThat(classRoomDTO1).isNotEqualTo(classRoomDTO2);
        classRoomDTO2.setId(classRoomDTO1.getId());
        assertThat(classRoomDTO1).isEqualTo(classRoomDTO2);
        classRoomDTO2.setId(2L);
        assertThat(classRoomDTO1).isNotEqualTo(classRoomDTO2);
        classRoomDTO1.setId(null);
        assertThat(classRoomDTO1).isNotEqualTo(classRoomDTO2);
    }
}
