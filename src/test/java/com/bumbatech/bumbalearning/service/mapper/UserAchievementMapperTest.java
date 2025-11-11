package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.UserAchievementAsserts.*;
import static com.bumbatech.bumbalearning.domain.UserAchievementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAchievementMapperTest {

    private UserAchievementMapper userAchievementMapper;

    @BeforeEach
    void setUp() {
        userAchievementMapper = new UserAchievementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserAchievementSample1();
        var actual = userAchievementMapper.toEntity(userAchievementMapper.toDto(expected));
        assertUserAchievementAllPropertiesEquals(expected, actual);
    }
}
