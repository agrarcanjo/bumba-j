package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.service.dto.UserAchievementDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bumbatech.bumbalearning.domain.UserAchievement}.
 */
public interface UserAchievementService {
    /**
     * Save a userAchievement.
     *
     * @param userAchievementDTO the entity to save.
     * @return the persisted entity.
     */
    UserAchievementDTO save(UserAchievementDTO userAchievementDTO);

    /**
     * Updates a userAchievement.
     *
     * @param userAchievementDTO the entity to update.
     * @return the persisted entity.
     */
    UserAchievementDTO update(UserAchievementDTO userAchievementDTO);

    /**
     * Partially updates a userAchievement.
     *
     * @param userAchievementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAchievementDTO> partialUpdate(UserAchievementDTO userAchievementDTO);

    /**
     * Get all the userAchievements.
     *
     * @return the list of entities.
     */
    List<UserAchievementDTO> findAll();

    /**
     * Get all the userAchievements with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAchievementDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userAchievement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAchievementDTO> findOne(Long id);

    /**
     * Delete the "id" userAchievement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
