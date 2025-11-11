package com.bumbatech.bumbalearning.web.rest;

import com.bumbatech.bumbalearning.repository.RecommendationRepository;
import com.bumbatech.bumbalearning.service.RecommendationService;
import com.bumbatech.bumbalearning.service.dto.RecommendationDTO;
import com.bumbatech.bumbalearning.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bumbatech.bumbalearning.domain.Recommendation}.
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationResource {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationResource.class);

    private static final String ENTITY_NAME = "recommendation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecommendationService recommendationService;

    private final RecommendationRepository recommendationRepository;

    public RecommendationResource(RecommendationService recommendationService, RecommendationRepository recommendationRepository) {
        this.recommendationService = recommendationService;
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * {@code POST  /recommendations} : Create a new recommendation.
     *
     * @param recommendationDTO the recommendationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recommendationDTO, or with status {@code 400 (Bad Request)} if the recommendation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RecommendationDTO> createRecommendation(@Valid @RequestBody RecommendationDTO recommendationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Recommendation : {}", recommendationDTO);
        if (recommendationDTO.getId() != null) {
            throw new BadRequestAlertException("A new recommendation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recommendationDTO = recommendationService.save(recommendationDTO);
        return ResponseEntity.created(new URI("/api/recommendations/" + recommendationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recommendationDTO.getId().toString()))
            .body(recommendationDTO);
    }

    /**
     * {@code PUT  /recommendations/:id} : Updates an existing recommendation.
     *
     * @param id the id of the recommendationDTO to save.
     * @param recommendationDTO the recommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommendationDTO,
     * or with status {@code 400 (Bad Request)} if the recommendationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecommendationDTO> updateRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RecommendationDTO recommendationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Recommendation : {}, {}", id, recommendationDTO);
        if (recommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recommendationDTO = recommendationService.update(recommendationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recommendationDTO.getId().toString()))
            .body(recommendationDTO);
    }

    /**
     * {@code PATCH  /recommendations/:id} : Partial updates given fields of an existing recommendation, field will ignore if it is null
     *
     * @param id the id of the recommendationDTO to save.
     * @param recommendationDTO the recommendationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recommendationDTO,
     * or with status {@code 400 (Bad Request)} if the recommendationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recommendationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recommendationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecommendationDTO> partialUpdateRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RecommendationDTO recommendationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Recommendation partially : {}, {}", id, recommendationDTO);
        if (recommendationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recommendationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecommendationDTO> result = recommendationService.partialUpdate(recommendationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recommendationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recommendations} : get all the recommendations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recommendations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RecommendationDTO>> getAllRecommendations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Recommendations");
        Page<RecommendationDTO> page;
        if (eagerload) {
            page = recommendationService.findAllWithEagerRelationships(pageable);
        } else {
            page = recommendationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recommendations/:id} : get the "id" recommendation.
     *
     * @param id the id of the recommendationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recommendationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecommendationDTO> getRecommendation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Recommendation : {}", id);
        Optional<RecommendationDTO> recommendationDTO = recommendationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recommendationDTO);
    }

    /**
     * {@code DELETE  /recommendations/:id} : delete the "id" recommendation.
     *
     * @param id the id of the recommendationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Recommendation : {}", id);
        recommendationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
