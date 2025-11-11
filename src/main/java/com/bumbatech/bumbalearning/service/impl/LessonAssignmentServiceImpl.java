package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.LessonAssignment;
import com.bumbatech.bumbalearning.repository.LessonAssignmentRepository;
import com.bumbatech.bumbalearning.service.LessonAssignmentService;
import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import com.bumbatech.bumbalearning.service.mapper.LessonAssignmentMapper;
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
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.LessonAssignment}.
 */
@Service
@Transactional
public class LessonAssignmentServiceImpl implements LessonAssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(LessonAssignmentServiceImpl.class);

    private final LessonAssignmentRepository lessonAssignmentRepository;

    private final LessonAssignmentMapper lessonAssignmentMapper;

    public LessonAssignmentServiceImpl(
        LessonAssignmentRepository lessonAssignmentRepository,
        LessonAssignmentMapper lessonAssignmentMapper
    ) {
        this.lessonAssignmentRepository = lessonAssignmentRepository;
        this.lessonAssignmentMapper = lessonAssignmentMapper;
    }

    @Override
    public LessonAssignmentDTO save(LessonAssignmentDTO lessonAssignmentDTO) {
        LOG.debug("Request to save LessonAssignment : {}", lessonAssignmentDTO);
        LessonAssignment lessonAssignment = lessonAssignmentMapper.toEntity(lessonAssignmentDTO);
        lessonAssignment = lessonAssignmentRepository.save(lessonAssignment);
        return lessonAssignmentMapper.toDto(lessonAssignment);
    }

    @Override
    public LessonAssignmentDTO update(LessonAssignmentDTO lessonAssignmentDTO) {
        LOG.debug("Request to update LessonAssignment : {}", lessonAssignmentDTO);
        LessonAssignment lessonAssignment = lessonAssignmentMapper.toEntity(lessonAssignmentDTO);
        lessonAssignment = lessonAssignmentRepository.save(lessonAssignment);
        return lessonAssignmentMapper.toDto(lessonAssignment);
    }

    @Override
    public Optional<LessonAssignmentDTO> partialUpdate(LessonAssignmentDTO lessonAssignmentDTO) {
        LOG.debug("Request to partially update LessonAssignment : {}", lessonAssignmentDTO);

        return lessonAssignmentRepository
            .findById(lessonAssignmentDTO.getId())
            .map(existingLessonAssignment -> {
                lessonAssignmentMapper.partialUpdate(existingLessonAssignment, lessonAssignmentDTO);

                return existingLessonAssignment;
            })
            .map(lessonAssignmentRepository::save)
            .map(lessonAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonAssignmentDTO> findAll() {
        LOG.debug("Request to get all LessonAssignments");
        return lessonAssignmentRepository
            .findAll()
            .stream()
            .map(lessonAssignmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<LessonAssignmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return lessonAssignmentRepository.findAllWithEagerRelationships(pageable).map(lessonAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LessonAssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get LessonAssignment : {}", id);
        return lessonAssignmentRepository.findOneWithEagerRelationships(id).map(lessonAssignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LessonAssignment : {}", id);
        lessonAssignmentRepository.deleteById(id);
    }
}
