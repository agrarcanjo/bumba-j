package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.ClassMemberAsserts.*;
import static com.bumbatech.bumbalearning.domain.ClassMemberTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassMemberMapperTest {

    private ClassMemberMapper classMemberMapper;

    @BeforeEach
    void setUp() {
        classMemberMapper = new ClassMemberMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClassMemberSample1();
        var actual = classMemberMapper.toEntity(classMemberMapper.toDto(expected));
        assertClassMemberAllPropertiesEquals(expected, actual);
    }
}
