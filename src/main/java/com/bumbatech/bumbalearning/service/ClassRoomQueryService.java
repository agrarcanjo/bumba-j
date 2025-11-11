package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.domain.*; // for static metamodels
import com.bumbatech.bumbalearning.domain.ClassRoom;
import com.bumbatech.bumbalearning.repository.ClassRoomRepository;
import com.bumbatech.bumbalearning.service.criteria.ClassRoomCriteria;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
import com.bumbatech.bumbalearning.service.mapper.ClassRoomMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ClassRoom} entities in the database.
 * The main input is a {@link ClassRoomCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClassRoomDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassRoomQueryService extends QueryService<ClassRoom> {

    private static final Logger LOG = LoggerFactory.getLogger(ClassRoomQueryService.class);

    private final ClassRoomRepository classRoomRepository;

    private final ClassRoomMapper classRoomMapper;

    public ClassRoomQueryService(ClassRoomRepository classRoomRepository, ClassRoomMapper classRoomMapper) {
        this.classRoomRepository = classRoomRepository;
        this.classRoomMapper = classRoomMapper;
    }

    /**
     * Return a {@link Page} of {@link ClassRoomDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassRoomDTO> findByCriteria(ClassRoomCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassRoom> specification = createSpecification(criteria);
        return classRoomRepository.findAll(specification, page).map(classRoomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassRoomCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ClassRoom> specification = createSpecification(criteria);
        return classRoomRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassRoomCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassRoom> createSpecification(ClassRoomCriteria criteria) {
        Specification<ClassRoom> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ClassRoom_.id),
                buildStringSpecification(criteria.getName(), ClassRoom_.name),
                buildSpecification(criteria.getLanguage(), ClassRoom_.language),
                buildRangeSpecification(criteria.getCreatedAt(), ClassRoom_.createdAt),
                buildStringSpecification(criteria.getDescription(), ClassRoom_.description),
                buildSpecification(criteria.getTeacherId(), root -> root.join(ClassRoom_.teacher, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
