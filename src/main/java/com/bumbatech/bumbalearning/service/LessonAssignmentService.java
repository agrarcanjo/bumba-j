package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bumbatech.bumbalearning.domain.LessonAssignment}.
 */
public interface LessonAssignmentService {
    /**
     * Save a lessonAssignment.
     *
     * @param lessonAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    LessonAssignmentDTO save(LessonAssignmentDTO lessonAssignmentDTO);

    /**
     * Updates a lessonAssignment.
     *
     * @param lessonAssignmentDTO the entity to update.
     * @return the persisted entity.
     */
    LessonAssignmentDTO update(LessonAssignmentDTO lessonAssignmentDTO);

    /**
     * Partially updates a lessonAssignment.
     *
     * @param lessonAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LessonAssignmentDTO> partialUpdate(LessonAssignmentDTO lessonAssignmentDTO);

    /**
     * Get all the lessonAssignments.
     *
     * @return the list of entities.
     */
    List<LessonAssignmentDTO> findAll();

    /**
     * Get all the lessonAssignments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LessonAssignmentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" lessonAssignment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LessonAssignmentDTO> findOne(Long id);

    /**
     * Delete the "id" lessonAssignment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
