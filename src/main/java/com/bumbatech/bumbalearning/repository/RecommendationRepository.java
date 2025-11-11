package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.Recommendation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Recommendation entity.
 */
@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    @Query("select recommendation from Recommendation recommendation where recommendation.student.login = ?#{authentication.name}")
    List<Recommendation> findByStudentIsCurrentUser();

    default Optional<Recommendation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Recommendation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Recommendation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select recommendation from Recommendation recommendation left join fetch recommendation.lesson",
        countQuery = "select count(recommendation) from Recommendation recommendation"
    )
    Page<Recommendation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select recommendation from Recommendation recommendation left join fetch recommendation.lesson")
    List<Recommendation> findAllWithToOneRelationships();

    @Query("select recommendation from Recommendation recommendation left join fetch recommendation.lesson where recommendation.id =:id")
    Optional<Recommendation> findOneWithToOneRelationships(@Param("id") Long id);
}
