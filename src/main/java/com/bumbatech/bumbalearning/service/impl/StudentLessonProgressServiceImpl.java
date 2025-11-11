package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.StudentLessonProgress;
import com.bumbatech.bumbalearning.repository.StudentLessonProgressRepository;
import com.bumbatech.bumbalearning.service.StudentLessonProgressService;
import com.bumbatech.bumbalearning.service.dto.StudentLessonProgressDTO;
import com.bumbatech.bumbalearning.service.mapper.StudentLessonProgressMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.StudentLessonProgress}.
 */
@Service
@Transactional
public class StudentLessonProgressServiceImpl implements StudentLessonProgressService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentLessonProgressServiceImpl.class);

    private final StudentLessonProgressRepository studentLessonProgressRepository;

    private final StudentLessonProgressMapper studentLessonProgressMapper;

    public StudentLessonProgressServiceImpl(
        StudentLessonProgressRepository studentLessonProgressRepository,
        StudentLessonProgressMapper studentLessonProgressMapper
    ) {
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.studentLessonProgressMapper = studentLessonProgressMapper;
    }

    @Override
    public StudentLessonProgressDTO save(StudentLessonProgressDTO studentLessonProgressDTO) {
        LOG.debug("Request to save StudentLessonProgress : {}", studentLessonProgressDTO);
        StudentLessonProgress studentLessonProgress = studentLessonProgressMapper.toEntity(studentLessonProgressDTO);
        studentLessonProgress = studentLessonProgressRepository.save(studentLessonProgress);
        return studentLessonProgressMapper.toDto(studentLessonProgress);
    }

    @Override
    public StudentLessonProgressDTO update(StudentLessonProgressDTO studentLessonProgressDTO) {
        LOG.debug("Request to update StudentLessonProgress : {}", studentLessonProgressDTO);
        StudentLessonProgress studentLessonProgress = studentLessonProgressMapper.toEntity(studentLessonProgressDTO);
        studentLessonProgress = studentLessonProgressRepository.save(studentLessonProgress);
        return studentLessonProgressMapper.toDto(studentLessonProgress);
    }

    @Override
    public Optional<StudentLessonProgressDTO> partialUpdate(StudentLessonProgressDTO studentLessonProgressDTO) {
        LOG.debug("Request to partially update StudentLessonProgress : {}", studentLessonProgressDTO);

        return studentLessonProgressRepository
            .findById(studentLessonProgressDTO.getId())
            .map(existingStudentLessonProgress -> {
                studentLessonProgressMapper.partialUpdate(existingStudentLessonProgress, studentLessonProgressDTO);

                return existingStudentLessonProgress;
            })
            .map(studentLessonProgressRepository::save)
            .map(studentLessonProgressMapper::toDto);
    }

    public Page<StudentLessonProgressDTO> findAllWithEagerRelationships(Pageable pageable) {
        return studentLessonProgressRepository.findAllWithEagerRelationships(pageable).map(studentLessonProgressMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentLessonProgressDTO> findOne(Long id) {
        LOG.debug("Request to get StudentLessonProgress : {}", id);
        return studentLessonProgressRepository.findOneWithEagerRelationships(id).map(studentLessonProgressMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete StudentLessonProgress : {}", id);
        studentLessonProgressRepository.deleteById(id);
    }
}
