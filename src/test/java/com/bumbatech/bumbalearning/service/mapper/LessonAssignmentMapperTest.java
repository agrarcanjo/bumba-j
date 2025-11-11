package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.LessonAssignmentAsserts.*;
import static com.bumbatech.bumbalearning.domain.LessonAssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LessonAssignmentMapperTest {

    private LessonAssignmentMapper lessonAssignmentMapper;

    @BeforeEach
    void setUp() {
        lessonAssignmentMapper = new LessonAssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLessonAssignmentSample1();
        var actual = lessonAssignmentMapper.toEntity(lessonAssignmentMapper.toDto(expected));
        assertLessonAssignmentAllPropertiesEquals(expected, actual);
    }
}
