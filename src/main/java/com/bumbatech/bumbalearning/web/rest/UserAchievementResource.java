package com.bumbatech.bumbalearning.web.rest;

import com.bumbatech.bumbalearning.repository.UserAchievementRepository;
import com.bumbatech.bumbalearning.service.UserAchievementService;
import com.bumbatech.bumbalearning.service.dto.UserAchievementDTO;
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
 * REST controller for managing {@link com.bumbatech.bumbalearning.domain.UserAchievement}.
 */
@RestController
@RequestMapping("/api/user-achievements")
public class UserAchievementResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserAchievementResource.class);

    private static final String ENTITY_NAME = "userAchievement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAchievementService userAchievementService;

    private final UserAchievementRepository userAchievementRepository;

    public UserAchievementResource(UserAchievementService userAchievementService, UserAchievementRepository userAchievementRepository) {
        this.userAchievementService = userAchievementService;
        this.userAchievementRepository = userAchievementRepository;
    }

    /**
     * {@code POST  /user-achievements} : Create a new userAchievement.
     *
     * @param userAchievementDTO the userAchievementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAchievementDTO, or with status {@code 400 (Bad Request)} if the userAchievement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAchievementDTO> createUserAchievement(@Valid @RequestBody UserAchievementDTO userAchievementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserAchievement : {}", userAchievementDTO);
        if (userAchievementDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAchievement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAchievementDTO = userAchievementService.save(userAchievementDTO);
        return ResponseEntity.created(new URI("/api/user-achievements/" + userAchievementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userAchievementDTO.getId().toString()))
            .body(userAchievementDTO);
    }

    /**
     * {@code PUT  /user-achievements/:id} : Updates an existing userAchievement.
     *
     * @param id the id of the userAchievementDTO to save.
     * @param userAchievementDTO the userAchievementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAchievementDTO,
     * or with status {@code 400 (Bad Request)} if the userAchievementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAchievementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAchievementDTO> updateUserAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAchievementDTO userAchievementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserAchievement : {}, {}", id, userAchievementDTO);
        if (userAchievementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAchievementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAchievementDTO = userAchievementService.update(userAchievementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAchievementDTO.getId().toString()))
            .body(userAchievementDTO);
    }

    /**
     * {@code PATCH  /user-achievements/:id} : Partial updates given fields of an existing userAchievement, field will ignore if it is null
     *
     * @param id the id of the userAchievementDTO to save.
     * @param userAchievementDTO the userAchievementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAchievementDTO,
     * or with status {@code 400 (Bad Request)} if the userAchievementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAchievementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAchievementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAchievementDTO> partialUpdateUserAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAchievementDTO userAchievementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserAchievement partially : {}, {}", id, userAchievementDTO);
        if (userAchievementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAchievementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAchievementDTO> result = userAchievementService.partialUpdate(userAchievementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAchievementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-achievements} : get all the userAchievements.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAchievements in body.
     */
    @GetMapping("")
    public List<UserAchievementDTO> getAllUserAchievements(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all UserAchievements");
        return userAchievementService.findAll();
    }

    /**
     * {@code GET  /user-achievements/:id} : get the "id" userAchievement.
     *
     * @param id the id of the userAchievementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAchievementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAchievementDTO> getUserAchievement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserAchievement : {}", id);
        Optional<UserAchievementDTO> userAchievementDTO = userAchievementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAchievementDTO);
    }

    /**
     * {@code DELETE  /user-achievements/:id} : delete the "id" userAchievement.
     *
     * @param id the id of the userAchievementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAchievement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserAchievement : {}", id);
        userAchievementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
