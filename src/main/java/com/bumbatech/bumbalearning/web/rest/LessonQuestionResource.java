package com.bumbatech.bumbalearning.web.rest;

import com.bumbatech.bumbalearning.repository.LessonQuestionRepository;
import com.bumbatech.bumbalearning.service.LessonQuestionService;
import com.bumbatech.bumbalearning.service.dto.LessonQuestionDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bumbatech.bumbalearning.domain.LessonQuestion}.
 */
@RestController
@RequestMapping("/api/lesson-questions")
public class LessonQuestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(LessonQuestionResource.class);

    private static final String ENTITY_NAME = "lessonQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonQuestionService lessonQuestionService;

    private final LessonQuestionRepository lessonQuestionRepository;

    public LessonQuestionResource(LessonQuestionService lessonQuestionService, LessonQuestionRepository lessonQuestionRepository) {
        this.lessonQuestionService = lessonQuestionService;
        this.lessonQuestionRepository = lessonQuestionRepository;
    }

    /**
     * {@code POST  /lesson-questions} : Create a new lessonQuestion.
     *
     * @param lessonQuestionDTO the lessonQuestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonQuestionDTO, or with status {@code 400 (Bad Request)} if the lessonQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LessonQuestionDTO> createLessonQuestion(@Valid @RequestBody LessonQuestionDTO lessonQuestionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LessonQuestion : {}", lessonQuestionDTO);
        if (lessonQuestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new lessonQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lessonQuestionDTO = lessonQuestionService.save(lessonQuestionDTO);
        return ResponseEntity.created(new URI("/api/lesson-questions/" + lessonQuestionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lessonQuestionDTO.getId().toString()))
            .body(lessonQuestionDTO);
    }

    /**
     * {@code PUT  /lesson-questions/:id} : Updates an existing lessonQuestion.
     *
     * @param id the id of the lessonQuestionDTO to save.
     * @param lessonQuestionDTO the lessonQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the lessonQuestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LessonQuestionDTO> updateLessonQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LessonQuestionDTO lessonQuestionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LessonQuestion : {}, {}", id, lessonQuestionDTO);
        if (lessonQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lessonQuestionDTO = lessonQuestionService.update(lessonQuestionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lessonQuestionDTO.getId().toString()))
            .body(lessonQuestionDTO);
    }

    /**
     * {@code PATCH  /lesson-questions/:id} : Partial updates given fields of an existing lessonQuestion, field will ignore if it is null
     *
     * @param id the id of the lessonQuestionDTO to save.
     * @param lessonQuestionDTO the lessonQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the lessonQuestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lessonQuestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lessonQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LessonQuestionDTO> partialUpdateLessonQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LessonQuestionDTO lessonQuestionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LessonQuestion partially : {}, {}", id, lessonQuestionDTO);
        if (lessonQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LessonQuestionDTO> result = lessonQuestionService.partialUpdate(lessonQuestionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lessonQuestionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-questions} : get all the lessonQuestions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonQuestions in body.
     */
    @GetMapping("")
    public List<LessonQuestionDTO> getAllLessonQuestions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all LessonQuestions");
        return lessonQuestionService.findAll();
    }

    /**
     * {@code GET  /lesson-questions/:id} : get the "id" lessonQuestion.
     *
     * @param id the id of the lessonQuestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonQuestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LessonQuestionDTO> getLessonQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LessonQuestion : {}", id);
        Optional<LessonQuestionDTO> lessonQuestionDTO = lessonQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lessonQuestionDTO);
    }

    /**
     * {@code DELETE  /lesson-questions/:id} : delete the "id" lessonQuestion.
     *
     * @param id the id of the lessonQuestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLessonQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LessonQuestion : {}", id);
        lessonQuestionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
