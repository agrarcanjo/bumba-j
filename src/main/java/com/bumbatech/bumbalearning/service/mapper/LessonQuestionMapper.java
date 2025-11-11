package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.LessonQuestion;
import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.dto.LessonQuestionDTO;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LessonQuestion} and its DTO {@link LessonQuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface LessonQuestionMapper extends EntityMapper<LessonQuestionDTO, LessonQuestion> {
    @Mapping(target = "lesson", source = "lesson", qualifiedByName = "lessonTitle")
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    LessonQuestionDTO toDto(LessonQuestion s);

    @Named("lessonTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    LessonDTO toDtoLessonTitle(Lesson lesson);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);
}
