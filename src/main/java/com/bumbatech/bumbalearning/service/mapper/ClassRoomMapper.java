package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassRoom} and its DTO {@link ClassRoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassRoomMapper extends EntityMapper<ClassRoomDTO, ClassRoom> {
    @Mapping(target = "teacher", source = "teacher", qualifiedByName = "userId")
    ClassRoomDTO toDto(ClassRoom s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
