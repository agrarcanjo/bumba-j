package com.bumbatech.bumbalearning.web.rest;

import com.bumbatech.bumbalearning.repository.StudentLessonProgressRepository;
import com.bumbatech.bumbalearning.service.StudentLessonProgressQueryService;
import com.bumbatech.bumbalearning.service.StudentLessonProgressService;
import com.bumbatech.bumbalearning.service.criteria.StudentLessonProgressCriteria;
import com.bumbatech.bumbalearning.service.dto.StudentLessonProgressDTO;
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
 * REST controller for managing {@link com.bumbatech.bumbalearning.domain.StudentLessonProgress}.
 */
@RestController
@RequestMapping("/api/student-lesson-progresses")
public class StudentLessonProgressResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentLessonProgressResource.class);

    private static final String ENTITY_NAME = "studentLessonProgress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentLessonProgressService studentLessonProgressService;

    private final StudentLessonProgressRepository studentLessonProgressRepository;

    private final StudentLessonProgressQueryService studentLessonProgressQueryService;

    public StudentLessonProgressResource(
        StudentLessonProgressService studentLessonProgressService,
        StudentLessonProgressRepository studentLessonProgressRepository,
        StudentLessonProgressQueryService studentLessonProgressQueryService
    ) {
        this.studentLessonProgressService = studentLessonProgressService;
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.studentLessonProgressQueryService = studentLessonProgressQueryService;
    }

    /**
     * {@code POST  /student-lesson-progresses} : Create a new studentLessonProgress.
     *
     * @param studentLessonProgressDTO the studentLessonProgressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentLessonProgressDTO, or with status {@code 400 (Bad Request)} if the studentLessonProgress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudentLessonProgressDTO> createStudentLessonProgress(
        @Valid @RequestBody StudentLessonProgressDTO studentLessonProgressDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save StudentLessonProgress : {}", studentLessonProgressDTO);
        if (studentLessonProgressDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentLessonProgress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        studentLessonProgressDTO = studentLessonProgressService.save(studentLessonProgressDTO);
        return ResponseEntity.created(new URI("/api/student-lesson-progresses/" + studentLessonProgressDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studentLessonProgressDTO.getId().toString()))
            .body(studentLessonProgressDTO);
    }

    /**
     * {@code PUT  /student-lesson-progresses/:id} : Updates an existing studentLessonProgress.
     *
     * @param id the id of the studentLessonProgressDTO to save.
     * @param studentLessonProgressDTO the studentLessonProgressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentLessonProgressDTO,
     * or with status {@code 400 (Bad Request)} if the studentLessonProgressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentLessonProgressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentLessonProgressDTO> updateStudentLessonProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentLessonProgressDTO studentLessonProgressDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudentLessonProgress : {}, {}", id, studentLessonProgressDTO);
        if (studentLessonProgressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentLessonProgressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentLessonProgressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentLessonProgressDTO = studentLessonProgressService.update(studentLessonProgressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentLessonProgressDTO.getId().toString()))
            .body(studentLessonProgressDTO);
    }

    /**
     * {@code PATCH  /student-lesson-progresses/:id} : Partial updates given fields of an existing studentLessonProgress, field will ignore if it is null
     *
     * @param id the id of the studentLessonProgressDTO to save.
     * @param studentLessonProgressDTO the studentLessonProgressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentLessonProgressDTO,
     * or with status {@code 400 (Bad Request)} if the studentLessonProgressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentLessonProgressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentLessonProgressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentLessonProgressDTO> partialUpdateStudentLessonProgress(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentLessonProgressDTO studentLessonProgressDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudentLessonProgress partially : {}, {}", id, studentLessonProgressDTO);
        if (studentLessonProgressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentLessonProgressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentLessonProgressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentLessonProgressDTO> result = studentLessonProgressService.partialUpdate(studentLessonProgressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentLessonProgressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-lesson-progresses} : get all the studentLessonProgresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentLessonProgresses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StudentLessonProgressDTO>> getAllStudentLessonProgresses(StudentLessonProgressCriteria criteria) {
        LOG.debug("REST request to get StudentLessonProgresses by criteria: {}", criteria);

        List<StudentLessonProgressDTO> entityList = studentLessonProgressQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /student-lesson-progresses/count} : count all the studentLessonProgresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countStudentLessonProgresses(StudentLessonProgressCriteria criteria) {
        LOG.debug("REST request to count StudentLessonProgresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(studentLessonProgressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /student-lesson-progresses/:id} : get the "id" studentLessonProgress.
     *
     * @param id the id of the studentLessonProgressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentLessonProgressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentLessonProgressDTO> getStudentLessonProgress(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudentLessonProgress : {}", id);
        Optional<StudentLessonProgressDTO> studentLessonProgressDTO = studentLessonProgressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentLessonProgressDTO);
    }

    /**
     * {@code DELETE  /student-lesson-progresses/:id} : delete the "id" studentLessonProgress.
     *
     * @param id the id of the studentLessonProgressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentLessonProgress(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudentLessonProgress : {}", id);
        studentLessonProgressService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
