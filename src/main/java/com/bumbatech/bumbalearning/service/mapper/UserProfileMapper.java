package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import com.bumbatech.bumbalearning.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UserProfileDTO toDto(UserProfile s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
