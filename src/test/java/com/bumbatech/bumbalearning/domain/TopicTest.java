package com.bumbatech.bumbalearning.domain;

import static com.bumbatech.bumbalearning.domain.TopicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TopicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topic.class);
        Topic topic1 = getTopicSample1();
        Topic topic2 = new Topic();
        assertThat(topic1).isNotEqualTo(topic2);

        topic2.setId(topic1.getId());
        assertThat(topic1).isEqualTo(topic2);

        topic2 = getTopicSample2();
        assertThat(topic1).isNotEqualTo(topic2);
    }
}
