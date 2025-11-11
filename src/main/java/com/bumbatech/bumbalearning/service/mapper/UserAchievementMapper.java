package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Achievement;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserAchievement;
import com.bumbatech.bumbalearning.service.dto.AchievementDTO;
import com.bumbatech.bumbalearning.service.dto.UserAchievementDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAchievement} and its DTO {@link UserAchievementDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAchievementMapper extends EntityMapper<UserAchievementDTO, UserAchievement> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "achievement", source = "achievement", qualifiedByName = "achievementName")
    UserAchievementDTO toDto(UserAchievement s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("achievementName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AchievementDTO toDtoAchievementName(Achievement achievement);
}
