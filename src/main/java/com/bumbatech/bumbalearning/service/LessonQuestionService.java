package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.service.dto.LessonQuestionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bumbatech.bumbalearning.domain.LessonQuestion}.
 */
public interface LessonQuestionService {
    /**
     * Save a lessonQuestion.
     *
     * @param lessonQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    LessonQuestionDTO save(LessonQuestionDTO lessonQuestionDTO);

    /**
     * Updates a lessonQuestion.
     *
     * @param lessonQuestionDTO the entity to update.
     * @return the persisted entity.
     */
    LessonQuestionDTO update(LessonQuestionDTO lessonQuestionDTO);

    /**
     * Partially updates a lessonQuestion.
     *
     * @param lessonQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LessonQuestionDTO> partialUpdate(LessonQuestionDTO lessonQuestionDTO);

    /**
     * Get all the lessonQuestions.
     *
     * @return the list of entities.
     */
    List<LessonQuestionDTO> findAll();

    /**
     * Get all the lessonQuestions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LessonQuestionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" lessonQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LessonQuestionDTO> findOne(Long id);

    /**
     * Delete the "id" lessonQuestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
