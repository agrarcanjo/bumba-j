package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.service.dto.StudentLessonProgressDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bumbatech.bumbalearning.domain.StudentLessonProgress}.
 */
public interface StudentLessonProgressService {
    /**
     * Save a studentLessonProgress.
     *
     * @param studentLessonProgressDTO the entity to save.
     * @return the persisted entity.
     */
    StudentLessonProgressDTO save(StudentLessonProgressDTO studentLessonProgressDTO);

    /**
     * Updates a studentLessonProgress.
     *
     * @param studentLessonProgressDTO the entity to update.
     * @return the persisted entity.
     */
    StudentLessonProgressDTO update(StudentLessonProgressDTO studentLessonProgressDTO);

    /**
     * Partially updates a studentLessonProgress.
     *
     * @param studentLessonProgressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StudentLessonProgressDTO> partialUpdate(StudentLessonProgressDTO studentLessonProgressDTO);

    /**
     * Get all the studentLessonProgresses with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentLessonProgressDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" studentLessonProgress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentLessonProgressDTO> findOne(Long id);

    /**
     * Delete the "id" studentLessonProgress.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
