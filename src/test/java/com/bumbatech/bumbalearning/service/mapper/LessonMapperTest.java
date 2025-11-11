package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.LessonAsserts.*;
import static com.bumbatech.bumbalearning.domain.LessonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LessonMapperTest {

    private LessonMapper lessonMapper;

    @BeforeEach
    void setUp() {
        lessonMapper = new LessonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLessonSample1();
        var actual = lessonMapper.toEntity(lessonMapper.toDto(expected));
        assertLessonAllPropertiesEquals(expected, actual);
    }
}
