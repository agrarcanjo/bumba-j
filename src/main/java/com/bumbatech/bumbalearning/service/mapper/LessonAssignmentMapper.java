package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.LessonAssignment;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LessonAssignment} and its DTO {@link LessonAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface LessonAssignmentMapper extends EntityMapper<LessonAssignmentDTO, LessonAssignment> {
    @Mapping(target = "classRoom", source = "classRoom", qualifiedByName = "classRoomName")
    @Mapping(target = "lesson", source = "lesson", qualifiedByName = "lessonTitle")
    @Mapping(target = "assignedBy", source = "assignedBy", qualifiedByName = "userId")
    LessonAssignmentDTO toDto(LessonAssignment s);

    @Named("classRoomName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClassRoomDTO toDtoClassRoomName(ClassRoom classRoom);

    @Named("lessonTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    LessonDTO toDtoLessonTitle(Lesson lesson);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
