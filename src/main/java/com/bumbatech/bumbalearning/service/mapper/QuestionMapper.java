package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.Topic;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import com.bumbatech.bumbalearning.service.dto.TopicDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "topic", source = "topic", qualifiedByName = "topicName")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "userId")
    QuestionDTO toDto(Question s);

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
