package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.LessonQuestionAsserts.*;
import static com.bumbatech.bumbalearning.domain.LessonQuestionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LessonQuestionMapperTest {

    private LessonQuestionMapper lessonQuestionMapper;

    @BeforeEach
    void setUp() {
        lessonQuestionMapper = new LessonQuestionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLessonQuestionSample1();
        var actual = lessonQuestionMapper.toEntity(lessonQuestionMapper.toDto(expected));
        assertLessonQuestionAllPropertiesEquals(expected, actual);
    }
}
