package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.domain.*; // for static metamodels
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import com.bumbatech.bumbalearning.service.criteria.UserProfileCriteria;
import com.bumbatech.bumbalearning.service.dto.UserProfileDTO;
import com.bumbatech.bumbalearning.service.mapper.UserProfileMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UserProfile} entities in the database.
 * The main input is a {@link UserProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserProfileQueryService extends QueryService<UserProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileQueryService.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    public UserProfileQueryService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    /**
     * Return a {@link List} of {@link UserProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> findByCriteria(UserProfileCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileMapper.toDto(userProfileRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserProfile> specification = createSpecification(criteria);
        return userProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link UserProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserProfile> createSpecification(UserProfileCriteria criteria) {
        Specification<UserProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UserProfile_.id),
                buildStringSpecification(criteria.getMunicipalityCode(), UserProfile_.municipalityCode),
                buildSpecification(criteria.getCurrentLevel(), UserProfile_.currentLevel),
                buildRangeSpecification(criteria.getTotalXp(), UserProfile_.totalXp),
                buildRangeSpecification(criteria.getCurrentStreak(), UserProfile_.currentStreak),
                buildRangeSpecification(criteria.getDailyGoalXp(), UserProfile_.dailyGoalXp),
                buildRangeSpecification(criteria.getLastActivityDate(), UserProfile_.lastActivityDate),
                buildSpecification(criteria.getUserId(), root -> root.join(UserProfile_.user, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
