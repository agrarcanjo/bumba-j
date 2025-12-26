package com.bumbatech.bumbalearning.web.rest;

import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.LessonAssignmentRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.LessonAssignmentService;
import com.bumbatech.bumbalearning.service.dto.LessonAssignmentDTO;
import com.bumbatech.bumbalearning.service.dto.UserDTO;
import com.bumbatech.bumbalearning.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
 * REST controller for managing {@link com.bumbatech.bumbalearning.domain.LessonAssignment}.
 */
@RestController
@RequestMapping("/api/lesson-assignments")
public class LessonAssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(LessonAssignmentResource.class);

    private static final String ENTITY_NAME = "lessonAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LessonAssignmentService lessonAssignmentService;

    private final LessonAssignmentRepository lessonAssignmentRepository;
    private final UserRepository userRepository;

    public LessonAssignmentResource(
        LessonAssignmentService lessonAssignmentService,
        LessonAssignmentRepository lessonAssignmentRepository,
        UserRepository userRepository
    ) {
        this.lessonAssignmentService = lessonAssignmentService;
        this.lessonAssignmentRepository = lessonAssignmentRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /lesson-assignments} : Create a new lessonAssignment.
     *
     * @param lessonAssignmentDTO the lessonAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lessonAssignmentDTO, or with status {@code 400 (Bad Request)} if the lessonAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LessonAssignmentDTO> createLessonAssignment(@Valid @RequestBody LessonAssignmentDTO lessonAssignmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LessonAssignment : {}", lessonAssignmentDTO);
        if (lessonAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new lessonAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }

        lessonAssignmentDTO.setAssignedAt(Instant.now());

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User not authenticated"));

        User user = userRepository.findOneByLogin(userLogin).orElseThrow(() -> new RuntimeException("User not found: " + userLogin));

        lessonAssignmentDTO.setAssignedBy(new UserDTO(user));

        lessonAssignmentDTO = lessonAssignmentService.save(lessonAssignmentDTO);
        return ResponseEntity.created(new URI("/api/lesson-assignments/" + lessonAssignmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lessonAssignmentDTO.getId().toString()))
            .body(lessonAssignmentDTO);
    }

    /**
     * {@code PUT  /lesson-assignments/:id} : Updates an existing lessonAssignment.
     *
     * @param id the id of the lessonAssignmentDTO to save.
     * @param lessonAssignmentDTO the lessonAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the lessonAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lessonAssignmentDTO couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LessonAssignmentDTO> updateLessonAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LessonAssignmentDTO lessonAssignmentDTO
    ) {
        LOG.debug("REST request to update LessonAssignment : {}, {}", id, lessonAssignmentDTO);
        if (lessonAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lessonAssignmentDTO = lessonAssignmentService.update(lessonAssignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lessonAssignmentDTO.getId().toString()))
            .body(lessonAssignmentDTO);
    }

    /**
     * {@code PATCH  /lesson-assignments/:id} : Partial updates given fields of an existing lessonAssignment, field will ignore if it is null
     *
     * @param id the id of the lessonAssignmentDTO to save.
     * @param lessonAssignmentDTO the lessonAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lessonAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the lessonAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lessonAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lessonAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LessonAssignmentDTO> partialUpdateLessonAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LessonAssignmentDTO lessonAssignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LessonAssignment partially : {}, {}", id, lessonAssignmentDTO);
        if (lessonAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lessonAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lessonAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LessonAssignmentDTO> result = lessonAssignmentService.partialUpdate(lessonAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lessonAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lesson-assignments} : get all the lessonAssignments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lessonAssignments in body.
     */
    @GetMapping("")
    public List<LessonAssignmentDTO> getAllLessonAssignments(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all LessonAssignments");
        return lessonAssignmentService.findAll();
    }

    /**
     * {@code GET  /lesson-assignments/:id} : get the "id" lessonAssignment.
     *
     * @param id the id of the lessonAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lessonAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LessonAssignmentDTO> getLessonAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LessonAssignment : {}", id);
        Optional<LessonAssignmentDTO> lessonAssignmentDTO = lessonAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lessonAssignmentDTO);
    }

    /**
     * {@code DELETE  /lesson-assignments/:id} : delete the "id" lessonAssignment.
     *
     * @param id the id of the lessonAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLessonAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LessonAssignment : {}", id);
        lessonAssignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
