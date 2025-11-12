package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.Achievement;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    @Query("select a from Achievement a where a.code = :code")
    Optional<Achievement> findByCode(@Param("code") String code);
}
