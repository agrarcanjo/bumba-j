package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.StudentLessonProgressAsserts.*;
import static com.bumbatech.bumbalearning.domain.StudentLessonProgressTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentLessonProgressMapperTest {

    private StudentLessonProgressMapper studentLessonProgressMapper;

    @BeforeEach
    void setUp() {
        studentLessonProgressMapper = new StudentLessonProgressMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudentLessonProgressSample1();
        var actual = studentLessonProgressMapper.toEntity(studentLessonProgressMapper.toDto(expected));
        assertStudentLessonProgressAllPropertiesEquals(expected, actual);
    }
}
