package com.bumbatech.bumbalearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAchievementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAchievementDTO.class);
        UserAchievementDTO userAchievementDTO1 = new UserAchievementDTO();
        userAchievementDTO1.setId(1L);
        UserAchievementDTO userAchievementDTO2 = new UserAchievementDTO();
        assertThat(userAchievementDTO1).isNotEqualTo(userAchievementDTO2);
        userAchievementDTO2.setId(userAchievementDTO1.getId());
        assertThat(userAchievementDTO1).isEqualTo(userAchievementDTO2);
        userAchievementDTO2.setId(2L);
        assertThat(userAchievementDTO1).isNotEqualTo(userAchievementDTO2);
        userAchievementDTO1.setId(null);
        assertThat(userAchievementDTO1).isNotEqualTo(userAchievementDTO2);
    }
}
