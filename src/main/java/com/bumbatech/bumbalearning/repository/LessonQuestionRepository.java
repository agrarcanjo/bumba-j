package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.LessonQuestion;
import com.bumbatech.bumbalearning.domain.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LessonQuestion entity.
 */
@Repository
public interface LessonQuestionRepository extends JpaRepository<LessonQuestion, Long> {
    default Optional<LessonQuestion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LessonQuestion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LessonQuestion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select lessonQuestion from LessonQuestion lessonQuestion left join fetch lessonQuestion.lesson",
        countQuery = "select count(lessonQuestion) from LessonQuestion lessonQuestion"
    )
    Page<LessonQuestion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select lessonQuestion from LessonQuestion lessonQuestion left join fetch lessonQuestion.lesson")
    List<LessonQuestion> findAllWithToOneRelationships();

    @Query("select lessonQuestion from LessonQuestion lessonQuestion left join fetch lessonQuestion.lesson where lessonQuestion.id =:id")
    Optional<LessonQuestion> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select lq.question.id from LessonQuestion lq where lq.lesson.id = :lessonId order by lq.orderIndex")
    List<Long> findQuestionIdsByLessonId(@Param("lessonId") Long lessonId);
}
