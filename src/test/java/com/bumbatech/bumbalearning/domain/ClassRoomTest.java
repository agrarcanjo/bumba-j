package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.ClassRoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassRoom.class);
        ClassRoom classRoom1 = getClassRoomSample1();
        ClassRoom classRoom2 = new ClassRoom();
        assertThat(classRoom1).isNotEqualTo(classRoom2);

        classRoom2.setId(classRoom1.getId());
        assertThat(classRoom1).isEqualTo(classRoom2);

        classRoom2 = getClassRoomSample2();
        assertThat(classRoom1).isNotEqualTo(classRoom2);
    }
}
