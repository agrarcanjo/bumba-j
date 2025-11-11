package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Attempt;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.AttemptDTO;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attempt} and its DTO {@link AttemptDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttemptMapper extends EntityMapper<AttemptDTO, Attempt> {
    @Mapping(target = "student", source = "student", qualifiedByName = "userId")
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    @Mapping(target = "lesson", source = "lesson", qualifiedByName = "lessonTitle")
    AttemptDTO toDto(Attempt s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);

    @Named("lessonTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    LessonDTO toDtoLessonTitle(Lesson lesson);
}
