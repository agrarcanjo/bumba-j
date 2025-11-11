package com.bumbatech.bumbalearning.service.mapper;

import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.LessonAssignment;
import com.bumbatech.bumbalearning.domain.StudentLessonProgress;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.dto.StudentLessonProgressDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentLessonProgress} and its DTO {@link StudentLessonProgressDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentLessonProgressMapper extends EntityMapper<StudentLessonProgressDTO, StudentLessonProgress> {
    @Mapping(target = "student", source = "student", qualifiedByName = "userId")
    @Mapping(target = "lesson", source = "lesson", qualifiedByName = "lessonTitle")
    @Mapping(target = "assignment", source = "assignment", qualifiedByName = "lessonAssignmentId")
    StudentLessonProgressDTO toDto(StudentLessonProgress s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("lessonTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    LessonDTO toDtoLessonTitle(Lesson lesson);

    @Named("lessonAssignmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LessonAssignmentDTO toDtoLessonAssignmentId(LessonAssignment lessonAssignment);
}
