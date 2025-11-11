package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.Topic;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.dto.TopicDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lesson} and its DTO {@link LessonDTO}.
 */
@Mapper(componentModel = "spring")
public interface LessonMapper extends EntityMapper<LessonDTO, Lesson> {
    @Mapping(target = "topic", source = "topic", qualifiedByName = "topicName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userId")
    LessonDTO toDto(Lesson s);

    @Named("topicName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TopicDTO toDtoTopicName(Topic topic);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
