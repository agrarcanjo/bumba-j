package com.bumbatech.bumbalearning.service.impl;

import com.bumbatech.bumbalearning.domain.UserAchievement;
import com.bumbatech.bumbalearning.repository.UserAchievementRepository;
import com.bumbatech.bumbalearning.service.UserAchievementService;
import com.bumbatech.bumbalearning.service.dto.UserAchievementDTO;
import com.bumbatech.bumbalearning.service.mapper.UserAchievementMapper;
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
 * Service Implementation for managing {@link com.bumbatech.bumbalearning.domain.UserAchievement}.
 */
@Service
@Transactional
public class UserAchievementServiceImpl implements UserAchievementService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAchievementServiceImpl.class);

    private final UserAchievementRepository userAchievementRepository;

    private final UserAchievementMapper userAchievementMapper;

    public UserAchievementServiceImpl(UserAchievementRepository userAchievementRepository, UserAchievementMapper userAchievementMapper) {
        this.userAchievementRepository = userAchievementRepository;
        this.userAchievementMapper = userAchievementMapper;
    }

    @Override
    public UserAchievementDTO save(UserAchievementDTO userAchievementDTO) {
        LOG.debug("Request to save UserAchievement : {}", userAchievementDTO);
        UserAchievement userAchievement = userAchievementMapper.toEntity(userAchievementDTO);
        userAchievement = userAchievementRepository.save(userAchievement);
        return userAchievementMapper.toDto(userAchievement);
    }

    @Override
    public UserAchievementDTO update(UserAchievementDTO userAchievementDTO) {
        LOG.debug("Request to update UserAchievement : {}", userAchievementDTO);
        UserAchievement userAchievement = userAchievementMapper.toEntity(userAchievementDTO);
        userAchievement = userAchievementRepository.save(userAchievement);
        return userAchievementMapper.toDto(userAchievement);
    }

    @Override
    public Optional<UserAchievementDTO> partialUpdate(UserAchievementDTO userAchievementDTO) {
        LOG.debug("Request to partially update UserAchievement : {}", userAchievementDTO);

        return userAchievementRepository
            .findById(userAchievementDTO.getId())
            .map(existingUserAchievement -> {
                userAchievementMapper.partialUpdate(existingUserAchievement, userAchievementDTO);

                return existingUserAchievement;
            })
            .map(userAchievementRepository::save)
            .map(userAchievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAchievementDTO> findAll() {
        LOG.debug("Request to get all UserAchievements");
        return userAchievementRepository
            .findAll()
            .stream()
            .map(userAchievementMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<UserAchievementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userAchievementRepository.findAllWithEagerRelationships(pageable).map(userAchievementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAchievementDTO> findOne(Long id) {
        LOG.debug("Request to get UserAchievement : {}", id);
        return userAchievementRepository.findOneWithEagerRelationships(id).map(userAchievementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete UserAchievement : {}", id);
        userAchievementRepository.deleteById(id);
    }
}
