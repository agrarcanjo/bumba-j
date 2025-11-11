package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.ClassMember;
import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.ClassMemberDTO;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassMember} and its DTO {@link ClassMemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassMemberMapper extends EntityMapper<ClassMemberDTO, ClassMember> {
    @Mapping(target = "classRoom", source = "classRoom", qualifiedByName = "classRoomName")
    @Mapping(target = "student", source = "student", qualifiedByName = "userId")
    ClassMemberDTO toDto(ClassMember s);

    @Named("classRoomName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClassRoomDTO toDtoClassRoomName(ClassRoom classRoom);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
