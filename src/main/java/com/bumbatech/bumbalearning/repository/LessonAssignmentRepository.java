package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.LessonAssignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LessonAssignment entity.
 */
@Repository
public interface LessonAssignmentRepository extends JpaRepository<LessonAssignment, Long> {
    @Query(
        "select lessonAssignment from LessonAssignment lessonAssignment where lessonAssignment.assignedBy.login = ?#{authentication.name}"
    )
    List<LessonAssignment> findByAssignedByIsCurrentUser();

    default Optional<LessonAssignment> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LessonAssignment> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LessonAssignment> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select lessonAssignment from LessonAssignment lessonAssignment left join fetch lessonAssignment.classRoom left join fetch lessonAssignment.lesson",
        countQuery = "select count(lessonAssignment) from LessonAssignment lessonAssignment"
    )
    Page<LessonAssignment> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select lessonAssignment from LessonAssignment lessonAssignment left join fetch lessonAssignment.classRoom left join fetch lessonAssignment.lesson"
    )
    List<LessonAssignment> findAllWithToOneRelationships();

    @Query(
        "select lessonAssignment from LessonAssignment lessonAssignment left join fetch lessonAssignment.classRoom left join fetch lessonAssignment.lesson where lessonAssignment.id =:id"
    )
    Optional<LessonAssignment> findOneWithToOneRelationships(@Param("id") Long id);

    long countByClassRoomId(Long classRoomId);

    List<LessonAssignment> findByClassRoomId(Long classRoomId);
}
