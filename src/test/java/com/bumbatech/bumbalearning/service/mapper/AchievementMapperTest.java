package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.AchievementAsserts.*;
import static com.bumbatech.bumbalearning.domain.AchievementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AchievementMapperTest {

    private AchievementMapper achievementMapper;

    @BeforeEach
    void setUp() {
        achievementMapper = new AchievementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAchievementSample1();
        var actual = achievementMapper.toEntity(achievementMapper.toDto(expected));
        assertAchievementAllPropertiesEquals(expected, actual);
    }
}
