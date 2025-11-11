package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.Attempt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attempt entity.
 */
@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long>, JpaSpecificationExecutor<Attempt> {
    @Query("select attempt from Attempt attempt where attempt.student.login = ?#{authentication.name}")
    List<Attempt> findByStudentIsCurrentUser();

    default Optional<Attempt> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Attempt> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Attempt> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select attempt from Attempt attempt left join fetch attempt.lesson",
        countQuery = "select count(attempt) from Attempt attempt"
    )
    Page<Attempt> findAllWithToOneRelationships(Pageable pageable);

    @Query("select attempt from Attempt attempt left join fetch attempt.lesson")
    List<Attempt> findAllWithToOneRelationships();

    @Query("select attempt from Attempt attempt left join fetch attempt.lesson where attempt.id =:id")
    Optional<Attempt> findOneWithToOneRelationships(@Param("id") Long id);
}
