package com.bumbatech.bumbalearning.web.rest;

import com.bumbatech.bumbalearning.repository.ClassRoomRepository;
import com.bumbatech.bumbalearning.service.ClassRoomQueryService;
import com.bumbatech.bumbalearning.service.ClassRoomService;
import com.bumbatech.bumbalearning.service.criteria.ClassRoomCriteria;
import com.bumbatech.bumbalearning.service.dto.ClassRoomDTO;
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
 * REST controller for managing {@link com.bumbatech.bumbalearning.domain.ClassRoom}.
 */
@RestController
@RequestMapping("/api/class-rooms")
public class ClassRoomResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClassRoomResource.class);

    private static final String ENTITY_NAME = "classRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassRoomService classRoomService;

    private final ClassRoomRepository classRoomRepository;

    private final ClassRoomQueryService classRoomQueryService;

    public ClassRoomResource(
        ClassRoomService classRoomService,
        ClassRoomRepository classRoomRepository,
        ClassRoomQueryService classRoomQueryService
    ) {
        this.classRoomService = classRoomService;
        this.classRoomRepository = classRoomRepository;
        this.classRoomQueryService = classRoomQueryService;
    }

    /**
     * {@code POST  /class-rooms} : Create a new classRoom.
     *
     * @param classRoomDTO the classRoomDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classRoomDTO, or with status {@code 400 (Bad Request)} if the classRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClassRoomDTO> createClassRoom(@Valid @RequestBody ClassRoomDTO classRoomDTO) throws URISyntaxException {
        LOG.debug("REST request to save ClassRoom : {}", classRoomDTO);
        if (classRoomDTO.getId() != null) {
            throw new BadRequestAlertException("A new classRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        classRoomDTO = classRoomService.save(classRoomDTO);
        return ResponseEntity.created(new URI("/api/class-rooms/" + classRoomDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, classRoomDTO.getId().toString()))
            .body(classRoomDTO);
    }

    /**
     * {@code PUT  /class-rooms/:id} : Updates an existing classRoom.
     *
     * @param id the id of the classRoomDTO to save.
     * @param classRoomDTO the classRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classRoomDTO,
     * or with status {@code 400 (Bad Request)} if the classRoomDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClassRoomDTO> updateClassRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassRoomDTO classRoomDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClassRoom : {}, {}", id, classRoomDTO);
        if (classRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        classRoomDTO = classRoomService.update(classRoomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classRoomDTO.getId().toString()))
            .body(classRoomDTO);
    }

    /**
     * {@code PATCH  /class-rooms/:id} : Partial updates given fields of an existing classRoom, field will ignore if it is null
     *
     * @param id the id of the classRoomDTO to save.
     * @param classRoomDTO the classRoomDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classRoomDTO,
     * or with status {@code 400 (Bad Request)} if the classRoomDTO is not valid,
     * or with status {@code 404 (Not Found)} if the classRoomDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the classRoomDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClassRoomDTO> partialUpdateClassRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClassRoomDTO classRoomDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClassRoom partially : {}, {}", id, classRoomDTO);
        if (classRoomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classRoomDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClassRoomDTO> result = classRoomService.partialUpdate(classRoomDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classRoomDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /class-rooms} : get all the classRooms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classRooms in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClassRoomDTO>> getAllClassRooms(
        ClassRoomCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ClassRooms by criteria: {}", criteria);

        Page<ClassRoomDTO> page = classRoomQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /class-rooms/count} : count all the classRooms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countClassRooms(ClassRoomCriteria criteria) {
        LOG.debug("REST request to count ClassRooms by criteria: {}", criteria);
        return ResponseEntity.ok().body(classRoomQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /class-rooms/:id} : get the "id" classRoom.
     *
     * @param id the id of the classRoomDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classRoomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClassRoomDTO> getClassRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClassRoom : {}", id);
        Optional<ClassRoomDTO> classRoomDTO = classRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classRoomDTO);
    }

    /**
     * {@code DELETE  /class-rooms/:id} : delete the "id" classRoom.
     *
     * @param id the id of the classRoomDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClassRoom : {}", id);
        classRoomService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
