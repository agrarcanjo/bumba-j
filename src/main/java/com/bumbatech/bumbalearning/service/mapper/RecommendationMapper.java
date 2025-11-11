package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.Recommendation;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.dto.RecommendationDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recommendation} and its DTO {@link RecommendationDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecommendationMapper extends EntityMapper<RecommendationDTO, Recommendation> {
    @Mapping(target = "student", source = "student", qualifiedByName = "userId")
    @Mapping(target = "lesson", source = "lesson", qualifiedByName = "lessonTitle")
    RecommendationDTO toDto(Recommendation s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("lessonTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    LessonDTO toDtoLessonTitle(Lesson lesson);
}
