package com.bumbatech.bumbalearning.service;

import com.bumbatech.bumbalearning.domain.*; // for static metamodels
import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.repository.QuestionRepository;
import com.bumbatech.bumbalearning.service.criteria.QuestionCriteria;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import com.bumbatech.bumbalearning.service.mapper.QuestionMapper;
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
 * Service for executing complex queries for {@link Question} entities in the database.
 * The main input is a {@link QuestionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuestionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionQueryService extends QueryService<Question> {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionQueryService.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    public QuestionQueryService(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    /**
     * Return a {@link Page} of {@link QuestionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findByCriteria(QuestionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Question> specification = createSpecification(criteria);
        return questionRepository.findAll(specification, page).map(questionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Question> specification = createSpecification(criteria);
        return questionRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Question> createSpecification(QuestionCriteria criteria) {
        Specification<Question> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Question_.id),
                buildSpecification(criteria.getType(), Question_.type),
                buildSpecification(criteria.getLanguage(), Question_.language),
                buildSpecification(criteria.getSkill(), Question_.skill),
                buildSpecification(criteria.getLevel(), Question_.level),
                buildRangeSpecification(criteria.getCreatedAt(), Question_.createdAt),
                buildStringSpecification(criteria.getPrompt(), Question_.prompt),
                buildSpecification(criteria.getTopicId(), root -> root.join(Question_.topic, JoinType.LEFT).get(Topic_.id)),
                buildSpecification(criteria.getCreatedById(), root -> root.join(Question_.createdBy, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
