package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.Recommendation;
import com.bumbatech.bumbalearning.repository.RecommendationRepository;
import com.bumbatech.bumbalearning.service.RecommendationService;
import com.bumbatech.bumbalearning.service.dto.RecommendationDTO;
import com.bumbatech.bumbalearning.service.mapper.RecommendationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.Recommendation}.
 */
@Service
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRepository recommendationRepository;

    private final RecommendationMapper recommendationMapper;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository, RecommendationMapper recommendationMapper) {
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    @Override
    public RecommendationDTO save(RecommendationDTO recommendationDTO) {
        LOG.debug("Request to save Recommendation : {}", recommendationDTO);
        Recommendation recommendation = recommendationMapper.toEntity(recommendationDTO);
        recommendation = recommendationRepository.save(recommendation);
        return recommendationMapper.toDto(recommendation);
    }

    @Override
    public RecommendationDTO update(RecommendationDTO recommendationDTO) {
        LOG.debug("Request to update Recommendation : {}", recommendationDTO);
        Recommendation recommendation = recommendationMapper.toEntity(recommendationDTO);
        recommendation = recommendationRepository.save(recommendation);
        return recommendationMapper.toDto(recommendation);
    }

    @Override
    public Optional<RecommendationDTO> partialUpdate(RecommendationDTO recommendationDTO) {
        LOG.debug("Request to partially update Recommendation : {}", recommendationDTO);

        return recommendationRepository
            .findById(recommendationDTO.getId())
            .map(existingRecommendation -> {
                recommendationMapper.partialUpdate(existingRecommendation, recommendationDTO);

                return existingRecommendation;
            })
            .map(recommendationRepository::save)
            .map(recommendationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecommendationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Recommendations");
        return recommendationRepository.findAll(pageable).map(recommendationMapper::toDto);
    }

    public Page<RecommendationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recommendationRepository.findAllWithEagerRelationships(pageable).map(recommendationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecommendationDTO> findOne(Long id) {
        LOG.debug("Request to get Recommendation : {}", id);
        return recommendationRepository.findOneWithEagerRelationships(id).map(recommendationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Recommendation : {}", id);
        recommendationRepository.deleteById(id);
    }
}
