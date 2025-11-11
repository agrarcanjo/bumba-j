package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.AttemptAsserts.*;
import static com.bumbatech.bumbalearning.domain.AttemptTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttemptMapperTest {

    private AttemptMapper attemptMapper;

    @BeforeEach
    void setUp() {
        attemptMapper = new AttemptMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAttemptSample1();
        var actual = attemptMapper.toEntity(attemptMapper.toDto(expected));
        assertAttemptAllPropertiesEquals(expected, actual);
    }
}
