package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.service.dto.TopicDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bumbatech.bumbalearning.domain.Topic}.
 */
public interface TopicService {
    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save.
     * @return the persisted entity.
     */
    TopicDTO save(TopicDTO topicDTO);

    /**
     * Updates a topic.
     *
     * @param topicDTO the entity to update.
     * @return the persisted entity.
     */
    TopicDTO update(TopicDTO topicDTO);

    /**
     * Partially updates a topic.
     *
     * @param topicDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TopicDTO> partialUpdate(TopicDTO topicDTO);

    /**
     * Get all the topics.
     *
     * @return the list of entities.
     */
    List<TopicDTO> findAll();

    /**
     * Get the "id" topic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopicDTO> findOne(Long id);

    /**
     * Delete the "id" topic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
