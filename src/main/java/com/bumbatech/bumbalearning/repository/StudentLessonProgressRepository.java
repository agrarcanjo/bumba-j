package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.StudentLessonProgress;
import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentLessonProgressRepository
    extends JpaRepository<StudentLessonProgress, Long>, JpaSpecificationExecutor<StudentLessonProgress> {
    @Query(
        "select studentLessonProgress from StudentLessonProgress studentLessonProgress where studentLessonProgress.student.login = ?#{authentication.name}"
    )
    List<StudentLessonProgress> findByStudentIsCurrentUser();

    default Optional<StudentLessonProgress> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<StudentLessonProgress> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<StudentLessonProgress> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select studentLessonProgress from StudentLessonProgress studentLessonProgress left join fetch studentLessonProgress.lesson",
        countQuery = "select count(studentLessonProgress) from StudentLessonProgress studentLessonProgress"
    )
    Page<StudentLessonProgress> findAllWithToOneRelationships(Pageable pageable);

    @Query("select studentLessonProgress from StudentLessonProgress studentLessonProgress left join fetch studentLessonProgress.lesson")
    List<StudentLessonProgress> findAllWithToOneRelationships();

    @Query(
        "select studentLessonProgress from StudentLessonProgress studentLessonProgress left join fetch studentLessonProgress.lesson where studentLessonProgress.id =:id"
    )
    Optional<StudentLessonProgress> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select count(slp) from StudentLessonProgress slp where slp.student.id = :userId and slp.status = 'COMPLETED'")
    long countCompletedLessonsByUserId(@Param("userId") Long userId);
}
