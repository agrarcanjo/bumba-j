package com.bumbatech.bumbalearning.repository;

import com.bumbatech.bumbalearning.domain.UserAchievement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    @Query("select userAchievement from UserAchievement userAchievement where userAchievement.user.login = ?#{authentication.name}")
    List<UserAchievement> findByUserIsCurrentUser();

    default Optional<UserAchievement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserAchievement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserAchievement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userAchievement from UserAchievement userAchievement left join fetch userAchievement.achievement",
        countQuery = "select count(userAchievement) from UserAchievement userAchievement"
    )
    Page<UserAchievement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select userAchievement from UserAchievement userAchievement left join fetch userAchievement.achievement")
    List<UserAchievement> findAllWithToOneRelationships();

    @Query(
        "select userAchievement from UserAchievement userAchievement left join fetch userAchievement.achievement where userAchievement.id =:id"
    )
    Optional<UserAchievement> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select case when count(ua) > 0 then true else false end from UserAchievement ua where ua.user.id = :userId and ua.achievement.id = :achievementId"
    )
    boolean existsByUserIdAndAchievementId(@Param("userId") Long userId, @Param("achievementId") Long achievementId);

    @Query(
        value = "select ua from UserAchievement ua left join fetch ua.achievement where ua.user.id = :userId order by ua.unlockedAt desc",
        countQuery = "select count(ua) from UserAchievement ua where ua.user.id = :userId"
    )
    Page<UserAchievement> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select ua from UserAchievement ua left join fetch ua.achievement where ua.user.id = :userId")
    List<UserAchievement> findByUserId(@Param("userId") Long userId);
}
