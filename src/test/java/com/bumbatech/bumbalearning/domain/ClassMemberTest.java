package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.ClassMemberTestSamples.*;
import static com.bumbatech.bumbalearning.domain.ClassRoomTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassMember.class);
        ClassMember classMember1 = getClassMemberSample1();
        ClassMember classMember2 = new ClassMember();
        assertThat(classMember1).isNotEqualTo(classMember2);

        classMember2.setId(classMember1.getId());
        assertThat(classMember1).isEqualTo(classMember2);

        classMember2 = getClassMemberSample2();
        assertThat(classMember1).isNotEqualTo(classMember2);
    }

    @Test
    void classRoomTest() {
        ClassMember classMember = getClassMemberRandomSampleGenerator();
        ClassRoom classRoomBack = getClassRoomRandomSampleGenerator();

        classMember.setClassRoom(classRoomBack);
        assertThat(classMember.getClassRoom()).isEqualTo(classRoomBack);

        classMember.classRoom(null);
        assertThat(classMember.getClassRoom()).isNull();
    }
}
