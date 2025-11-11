package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.Topic;
import com.bumbatech.bumbalearning.repository.TopicRepository;
import com.bumbatech.bumbalearning.service.TopicService;
import com.bumbatech.bumbalearning.service.dto.TopicDTO;
import com.bumbatech.bumbalearning.service.mapper.TopicMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.Topic}.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private static final Logger LOG = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;

    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        LOG.debug("Request to save Topic : {}", topicDTO);
        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    @Override
    public TopicDTO update(TopicDTO topicDTO) {
        LOG.debug("Request to update Topic : {}", topicDTO);
        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    @Override
    public Optional<TopicDTO> partialUpdate(TopicDTO topicDTO) {
        LOG.debug("Request to partially update Topic : {}", topicDTO);

        return topicRepository
            .findById(topicDTO.getId())
            .map(existingTopic -> {
                topicMapper.partialUpdate(existingTopic, topicDTO);

                return existingTopic;
            })
            .map(topicRepository::save)
            .map(topicMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicDTO> findAll() {
        LOG.debug("Request to get all Topics");
        return topicRepository.findAll().stream().map(topicMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(Long id) {
        LOG.debug("Request to get Topic : {}", id);
        return topicRepository.findById(id).map(topicMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
    }
}
