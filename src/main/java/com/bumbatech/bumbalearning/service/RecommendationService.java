package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.service.dto.RecommendationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bumbatech.bumbalearning.domain.Recommendation}.
 */
public interface RecommendationService {
    /**
     * Save a recommendation.
     *
     * @param recommendationDTO the entity to save.
     * @return the persisted entity.
     */
    RecommendationDTO save(RecommendationDTO recommendationDTO);

    /**
     * Updates a recommendation.
     *
     * @param recommendationDTO the entity to update.
     * @return the persisted entity.
     */
    RecommendationDTO update(RecommendationDTO recommendationDTO);

    /**
     * Partially updates a recommendation.
     *
     * @param recommendationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecommendationDTO> partialUpdate(RecommendationDTO recommendationDTO);

    /**
     * Get all the recommendations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecommendationDTO> findAll(Pageable pageable);

    /**
     * Get all the recommendations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecommendationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" recommendation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecommendationDTO> findOne(Long id);

    /**
     * Delete the "id" recommendation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
