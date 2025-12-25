package com.bumbatech.bumbalearning.web.rest.admin;

import com.bumbatech.bumbalearning.repository.AchievementRepository;
import com.bumbatech.bumbalearning.repository.QuestionRepository;
import com.bumbatech.bumbalearning.repository.TopicRepository;
import com.bumbatech.bumbalearning.security.AuthoritiesConstants;
import com.bumbatech.bumbalearning.service.AchievementService;
import com.bumbatech.bumbalearning.service.QuestionService;
import com.bumbatech.bumbalearning.service.TopicService;
import com.bumbatech.bumbalearning.service.dto.AchievementDTO;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import com.bumbatech.bumbalearning.service.dto.TopicDTO;
import com.bumbatech.bumbalearning.service.mapper.AchievementMapper;
import com.bumbatech.bumbalearning.service.mapper.QuestionMapper;
import com.bumbatech.bumbalearning.service.mapper.TopicMapper;
import com.bumbatech.bumbalearning.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/admin/content")
@PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
public class AdminContentResource {

    private static final Logger log = LoggerFactory.getLogger(AdminContentResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopicService topicService;
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final AchievementService achievementService;
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public AdminContentResource(
        TopicService topicService,
        TopicRepository topicRepository,
        TopicMapper topicMapper,
        AchievementService achievementService,
        AchievementRepository achievementRepository,
        AchievementMapper achievementMapper,
        QuestionService questionService,
        QuestionRepository questionRepository,
        QuestionMapper questionMapper
    ) {
        this.topicService = topicService;
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
        this.achievementService = achievementService;
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    @GetMapping("/topics")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        log.debug("REST request to get all Topics");
        var topics = topicRepository.findAll();
        var topicDTOs = topics.stream().map(topicMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(topicDTOs);
    }

    @PostMapping("/topics")
    public ResponseEntity<TopicDTO> createTopic(@Valid @RequestBody TopicDTO topicDTO) throws URISyntaxException {
        log.debug("REST request to save Topic : {}", topicDTO);
        if (topicDTO.getId() != null) {
            throw new BadRequestAlertException("A new topic cannot already have an ID", "topic", "idexists");
        }
        topicDTO = topicService.save(topicDTO);
        return ResponseEntity.created(new URI("/api/admin/content/topics/" + topicDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "topic", topicDTO.getId().toString()))
            .body(topicDTO);
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<TopicDTO> updateTopic(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TopicDTO topicDTO
    ) {
        log.debug("REST request to update Topic : {}, {}", id, topicDTO);
        if (topicDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "topic", "idnull");
        }
        if (!Objects.equals(id, topicDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", "topic", "idinvalid");
        }
        if (!topicRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");
        }
        topicDTO = topicService.update(topicDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "topic", topicDTO.getId().toString()))
            .body(topicDTO);
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        log.debug("REST request to delete Topic : {}", id);
        topicService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, "topic", id.toString()))
            .build();
    }

    @GetMapping("/achievements")
    public ResponseEntity<List<AchievementDTO>> getAllAchievements() {
        log.debug("REST request to get all Achievements");
        var achievements = achievementRepository.findAll();
        var achievementDTOs = achievements.stream().map(achievementMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(achievementDTOs);
    }

    @PostMapping("/achievements")
    public ResponseEntity<AchievementDTO> createAchievement(@Valid @RequestBody AchievementDTO achievementDTO) throws URISyntaxException {
        log.debug("REST request to save Achievement : {}", achievementDTO);
        if (achievementDTO.getId() != null) {
            throw new BadRequestAlertException("A new achievement cannot already have an ID", "achievement", "idexists");
        }
        achievementDTO = achievementService.save(achievementDTO);
        return ResponseEntity.created(new URI("/api/admin/content/achievements/" + achievementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "achievement", achievementDTO.getId().toString()))
            .body(achievementDTO);
    }

    @PutMapping("/achievements/{id}")
    public ResponseEntity<AchievementDTO> updateAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AchievementDTO achievementDTO
    ) {
        log.debug("REST request to update Achievement : {}, {}", id, achievementDTO);
        if (achievementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "achievement", "idnull");
        }
        if (!Objects.equals(id, achievementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", "achievement", "idinvalid");
        }
        if (!achievementRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Achievement not found");
        }
        achievementDTO = achievementService.update(achievementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "achievement", achievementDTO.getId().toString()))
            .body(achievementDTO);
    }

    @DeleteMapping("/achievements/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        log.debug("REST request to delete Achievement : {}", id);
        achievementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, "achievement", id.toString()))
            .build();
    }

    @GetMapping("/questions/pending")
    public ResponseEntity<List<QuestionDTO>> getPendingQuestions() {
        log.debug("REST request to get pending Questions for moderation");
        var questions = questionRepository.findAll();
        var questionDTOs = questions.stream().map(questionMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(questionDTOs);
    }

    @PutMapping("/questions/{id}/approve")
    public ResponseEntity<QuestionDTO> approveQuestion(@PathVariable Long id) {
        log.debug("REST request to approve Question : {}", id);
        var questionDTO = questionService
            .findOne(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, "question", id.toString()))
            .body(questionDTO);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> rejectQuestion(@PathVariable Long id) {
        log.debug("REST request to reject Question : {}", id);
        questionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, "question", id.toString()))
            .build();
    }
}
