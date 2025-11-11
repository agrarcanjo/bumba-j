package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.RecommendationAsserts.*;
import static com.bumbatech.bumbalearning.domain.RecommendationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecommendationMapperTest {

    private RecommendationMapper recommendationMapper;

    @BeforeEach
    void setUp() {
        recommendationMapper = new RecommendationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecommendationSample1();
        var actual = recommendationMapper.toEntity(recommendationMapper.toDto(expected));
        assertRecommendationAllPropertiesEquals(expected, actual);
    }
}
