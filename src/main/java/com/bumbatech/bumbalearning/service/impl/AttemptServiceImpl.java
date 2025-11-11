package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.Attempt;
import com.bumbatech.bumbalearning.repository.AttemptRepository;
import com.bumbatech.bumbalearning.service.AttemptService;
import com.bumbatech.bumbalearning.service.dto.AttemptDTO;
import com.bumbatech.bumbalearning.service.mapper.AttemptMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.Attempt}.
 */
@Service
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private static final Logger LOG = LoggerFactory.getLogger(AttemptServiceImpl.class);

    private final AttemptRepository attemptRepository;

    private final AttemptMapper attemptMapper;

    public AttemptServiceImpl(AttemptRepository attemptRepository, AttemptMapper attemptMapper) {
        this.attemptRepository = attemptRepository;
        this.attemptMapper = attemptMapper;
    }

    @Override
    public AttemptDTO save(AttemptDTO attemptDTO) {
        LOG.debug("Request to save Attempt : {}", attemptDTO);
        Attempt attempt = attemptMapper.toEntity(attemptDTO);
        attempt = attemptRepository.save(attempt);
        return attemptMapper.toDto(attempt);
    }

    @Override
    public AttemptDTO update(AttemptDTO attemptDTO) {
        LOG.debug("Request to update Attempt : {}", attemptDTO);
        Attempt attempt = attemptMapper.toEntity(attemptDTO);
        attempt = attemptRepository.save(attempt);
        return attemptMapper.toDto(attempt);
    }

    @Override
    public Optional<AttemptDTO> partialUpdate(AttemptDTO attemptDTO) {
        LOG.debug("Request to partially update Attempt : {}", attemptDTO);

        return attemptRepository
            .findById(attemptDTO.getId())
            .map(existingAttempt -> {
                attemptMapper.partialUpdate(existingAttempt, attemptDTO);

                return existingAttempt;
            })
            .map(attemptRepository::save)
            .map(attemptMapper::toDto);
    }

    public Page<AttemptDTO> findAllWithEagerRelationships(Pageable pageable) {
        return attemptRepository.findAllWithEagerRelationships(pageable).map(attemptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttemptDTO> findOne(Long id) {
        LOG.debug("Request to get Attempt : {}", id);
        return attemptRepository.findOneWithEagerRelationships(id).map(attemptMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Attempt : {}", id);
        attemptRepository.deleteById(id);
    }
}
