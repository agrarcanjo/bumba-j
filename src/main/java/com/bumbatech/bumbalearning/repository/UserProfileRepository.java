package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.UserProfile;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {
    @Query("select up from UserProfile up where up.user.id = :userId")
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);

    @Query(
        "select up from UserProfile up left join fetch up.user " +
        "where up.municipalityCode = :municipalityCode " +
        "order by up.totalXp desc, up.currentStreak desc"
    )
    List<UserProfile> findTopUsersByMunicipality(@Param("municipalityCode") String municipalityCode, Pageable pageable);

    @Query(
        "select up from UserProfile up left join fetch up.user " +
        "where up.municipalityCode = :municipalityCode " +
        "and up.lastActivityDate >= :periodStart " +
        "order by up.totalXp desc, up.currentStreak desc"
    )
    List<UserProfile> findTopUsersByMunicipalityAndPeriod(
        @Param("municipalityCode") String municipalityCode,
        @Param("periodStart") Instant periodStart,
        Pageable pageable
    );

    @Query("select count(up) from UserProfile up where up.municipalityCode = :municipalityCode")
    Long countByMunicipality(@Param("municipalityCode") String municipalityCode);

    @Query("select count(up) from UserProfile up where up.municipalityCode = :municipalityCode and up.lastActivityDate >= :periodStart")
    Long countByMunicipalityAndPeriod(@Param("municipalityCode") String municipalityCode, @Param("periodStart") Instant periodStart);
}
