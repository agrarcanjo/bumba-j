package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.domain.*; // for static metamodels
import com.bumbatech.bumbalearning.domain.StudentLessonProgress;
import com.bumbatech.bumbalearning.repository.StudentLessonProgressRepository;
import com.bumbatech.bumbalearning.service.criteria.StudentLessonProgressCriteria;
import com.bumbatech.bumbalearning.service.dto.StudentLessonProgressDTO;
import com.bumbatech.bumbalearning.service.mapper.StudentLessonProgressMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StudentLessonProgress} entities in the database.
 * The main input is a {@link StudentLessonProgressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StudentLessonProgressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentLessonProgressQueryService extends QueryService<StudentLessonProgress> {

    private static final Logger LOG = LoggerFactory.getLogger(StudentLessonProgressQueryService.class);

    private final StudentLessonProgressRepository studentLessonProgressRepository;

    private final StudentLessonProgressMapper studentLessonProgressMapper;

    public StudentLessonProgressQueryService(
        StudentLessonProgressRepository studentLessonProgressRepository,
        StudentLessonProgressMapper studentLessonProgressMapper
    ) {
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.studentLessonProgressMapper = studentLessonProgressMapper;
    }

    /**
     * Return a {@link List} of {@link StudentLessonProgressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentLessonProgressDTO> findByCriteria(StudentLessonProgressCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<StudentLessonProgress> specification = createSpecification(criteria);
        return studentLessonProgressMapper.toDto(studentLessonProgressRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StudentLessonProgressCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<StudentLessonProgress> specification = createSpecification(criteria);
        return studentLessonProgressRepository.count(specification);
    }

    /**
     * Function to convert {@link StudentLessonProgressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StudentLessonProgress> createSpecification(StudentLessonProgressCriteria criteria) {
        Specification<StudentLessonProgress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), StudentLessonProgress_.id),
                buildSpecification(criteria.getStatus(), StudentLessonProgress_.status),
                buildRangeSpecification(criteria.getScore(), StudentLessonProgress_.score),
                buildRangeSpecification(criteria.getXpEarned(), StudentLessonProgress_.xpEarned),
                buildRangeSpecification(criteria.getCompletedAt(), StudentLessonProgress_.completedAt),
                buildSpecification(criteria.getIsLate(), StudentLessonProgress_.isLate),
                buildSpecification(criteria.getStudentId(), root -> root.join(StudentLessonProgress_.student, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getLessonId(), root -> root.join(StudentLessonProgress_.lesson, JoinType.LEFT).get(Lesson_.id)),
                buildSpecification(criteria.getAssignmentId(), root ->
                    root.join(StudentLessonProgress_.assignment, JoinType.LEFT).get(LessonAssignment_.id)
                )
            );
        }
        return specification;
    }
}
