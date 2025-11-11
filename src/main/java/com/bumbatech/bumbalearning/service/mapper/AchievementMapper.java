package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Achievement;
import com.bumbatech.bumbalearning.service.dto.AchievementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Achievement} and its DTO {@link AchievementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AchievementMapper extends EntityMapper<AchievementDTO, Achievement> {}
