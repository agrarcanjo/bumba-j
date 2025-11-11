package com.bumbatech.bumbalearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttemptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttemptDTO.class);
        AttemptDTO attemptDTO1 = new AttemptDTO();
        attemptDTO1.setId(1L);
        AttemptDTO attemptDTO2 = new AttemptDTO();
        assertThat(attemptDTO1).isNotEqualTo(attemptDTO2);
        attemptDTO2.setId(attemptDTO1.getId());
        assertThat(attemptDTO1).isEqualTo(attemptDTO2);
        attemptDTO2.setId(2L);
        assertThat(attemptDTO1).isNotEqualTo(attemptDTO2);
        attemptDTO1.setId(null);
        assertThat(attemptDTO1).isNotEqualTo(attemptDTO2);
    }
}
