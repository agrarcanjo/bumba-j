package com.bumbatech.bumbalearning.service.mapper;

import static com.bumbatech.bumbalearning.domain.TopicAsserts.*;
import static com.bumbatech.bumbalearning.domain.TopicTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TopicMapperTest {

    private TopicMapper topicMapper;

    @BeforeEach
    void setUp() {
        topicMapper = new TopicMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTopicSample1();
        var actual = topicMapper.toEntity(topicMapper.toDto(expected));
        assertTopicAllPropertiesEquals(expected, actual);
    }
}
