package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.StudentLessonProgress;
import com.bumbatech.bumbalearning.domain.enumeration.LessonStatus;
import java.time.Instant;
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

    List<StudentLessonProgress> findByStudentIdAndStatus(Long studentId, LessonStatus status);

    List<StudentLessonProgress> findTop10ByStudentIdOrderByCompletedAtDesc(Long studentId);

    @Query(
        "select slp from StudentLessonProgress slp " +
        "join fetch slp.student " +
        "join fetch slp.lesson " +
        "join ClassMember cm on cm.student.id = slp.student.id " +
        "join ClassRoom cr on cr.id = cm.classRoom.id " +
        "where cr.teacher.id = :teacherId " +
        "and slp.completedAt >= :since " +
        "and slp.status = 'COMPLETED' " +
        "order by slp.completedAt desc"
    )
    List<StudentLessonProgress> findRecentActivitiesByTeacherId(@Param("teacherId") Long teacherId, @Param("since") Instant since);

    @Query(
        "select count(slp) from StudentLessonProgress slp " +
        "join ClassMember cm on cm.student.id = slp.student.id " +
        "where cm.classRoom.id = :classRoomId " +
        "and slp.status = 'COMPLETED'"
    )
    long countCompletedLessonsByClassRoomId(@Param("classRoomId") Long classRoomId);

    @Query(
        "select slp from StudentLessonProgress slp " +
        "join fetch slp.lesson " +
        "join ClassMember cm on cm.student.id = slp.student.id " +
        "where cm.classRoom.id = :classRoomId " +
        "and slp.student.id = :studentId " +
        "and slp.status = 'COMPLETED' " +
        "order by slp.completedAt desc"
    )
    List<StudentLessonProgress> findByClassRoomIdAndStudentId(@Param("classRoomId") Long classRoomId, @Param("studentId") Long studentId);

    @Query("select avg(slp.score) from StudentLessonProgress slp " + "where slp.student.id = :studentId " + "and slp.status = 'COMPLETED'")
    Double findAverageScoreByStudentId(@Param("studentId") Long studentId);

    @Query(
        "select avg(slp.score) from StudentLessonProgress slp " +
        "join ClassMember cm on cm.student.id = slp.student.id " +
        "where cm.classRoom.id = :classRoomId " +
        "and slp.status = 'COMPLETED'"
    )
    Double findAverageScoreByClassRoomId(@Param("classRoomId") Long classRoomId);

    List<StudentLessonProgress> findByStudentIdAndStatusOrderByCompletedAtDesc(Long studentId, LessonStatus status);
}
