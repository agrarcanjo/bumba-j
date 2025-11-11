package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.LessonQuestion;
import com.bumbatech.bumbalearning.repository.LessonQuestionRepository;
import com.bumbatech.bumbalearning.service.LessonQuestionService;
import com.bumbatech.bumbalearning.service.dto.LessonQuestionDTO;
import com.bumbatech.bumbalearning.service.mapper.LessonQuestionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.LessonQuestion}.
 */
@Service
@Transactional
public class LessonQuestionServiceImpl implements LessonQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonQuestionServiceImpl.class);

    private final LessonQuestionRepository lessonQuestionRepository;

    private final LessonQuestionMapper lessonQuestionMapper;

    public LessonQuestionServiceImpl(LessonQuestionRepository lessonQuestionRepository, LessonQuestionMapper lessonQuestionMapper) {
        this.lessonQuestionRepository = lessonQuestionRepository;
        this.lessonQuestionMapper = lessonQuestionMapper;
    }

    @Override
    public LessonQuestionDTO save(LessonQuestionDTO lessonQuestionDTO) {
        LOG.debug("Request to save LessonQuestion : {}", lessonQuestionDTO);
        LessonQuestion lessonQuestion = lessonQuestionMapper.toEntity(lessonQuestionDTO);
        lessonQuestion = lessonQuestionRepository.save(lessonQuestion);
        return lessonQuestionMapper.toDto(lessonQuestion);
    }

    @Override
    public LessonQuestionDTO update(LessonQuestionDTO lessonQuestionDTO) {
        LOG.debug("Request to update LessonQuestion : {}", lessonQuestionDTO);
        LessonQuestion lessonQuestion = lessonQuestionMapper.toEntity(lessonQuestionDTO);
        lessonQuestion = lessonQuestionRepository.save(lessonQuestion);
        return lessonQuestionMapper.toDto(lessonQuestion);
    }

    @Override
    public Optional<LessonQuestionDTO> partialUpdate(LessonQuestionDTO lessonQuestionDTO) {
        LOG.debug("Request to partially update LessonQuestion : {}", lessonQuestionDTO);

        return lessonQuestionRepository
            .findById(lessonQuestionDTO.getId())
            .map(existingLessonQuestion -> {
                lessonQuestionMapper.partialUpdate(existingLessonQuestion, lessonQuestionDTO);

                return existingLessonQuestion;
            })
            .map(lessonQuestionRepository::save)
            .map(lessonQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonQuestionDTO> findAll() {
        LOG.debug("Request to get all LessonQuestions");
        return lessonQuestionRepository
            .findAll()
            .stream()
            .map(lessonQuestionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<LessonQuestionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return lessonQuestionRepository.findAllWithEagerRelationships(pageable).map(lessonQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LessonQuestionDTO> findOne(Long id) {
        LOG.debug("Request to get LessonQuestion : {}", id);
        return lessonQuestionRepository.findOneWithEagerRelationships(id).map(lessonQuestionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LessonQuestion : {}", id);
        lessonQuestionRepository.deleteById(id);
    }
}
