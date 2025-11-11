package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bumbatech.bumbalearning.domain.ClassRoom}.
 */
public interface ClassRoomService {
    /**
     * Save a classRoom.
     *
     * @param classRoomDTO the entity to save.
     * @return the persisted entity.
     */
    ClassRoomDTO save(ClassRoomDTO classRoomDTO);

    /**
     * Updates a classRoom.
     *
     * @param classRoomDTO the entity to update.
     * @return the persisted entity.
     */
    ClassRoomDTO update(ClassRoomDTO classRoomDTO);

    /**
     * Partially updates a classRoom.
     *
     * @param classRoomDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClassRoomDTO> partialUpdate(ClassRoomDTO classRoomDTO);

    /**
     * Get the "id" classRoom.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassRoomDTO> findOne(Long id);

    /**
     * Delete the "id" classRoom.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
