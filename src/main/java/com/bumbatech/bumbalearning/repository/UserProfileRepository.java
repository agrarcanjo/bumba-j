package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {
    @Query("select up from UserProfile up where up.user.id = :userId")
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);
}
