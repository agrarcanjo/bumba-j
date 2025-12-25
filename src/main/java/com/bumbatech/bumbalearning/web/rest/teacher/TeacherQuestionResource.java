package com.bumbatech.bumbalearning.web.rest.teacher;

import com.bumbatech.bumbalearning.repository.QuestionRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.QuestionService;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import com.bumbatech.bumbalearning.service.mapper.QuestionMapper;
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
@RequestMapping("/api/teacher/questions")
@PreAuthorize("hasAuthority('ROLE_TEACHER')")
public class TeacherQuestionResource {

    private static final Logger log = LoggerFactory.getLogger(TeacherQuestionResource.class);
    private static final String ENTITY_NAME = "question";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final UserRepository userRepository;

    public TeacherQuestionResource(
        QuestionService questionService,
        QuestionRepository questionRepository,
        QuestionMapper questionMapper,
        UserRepository userRepository
    ) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllTeacherQuestions() {
        log.debug("REST request to get all questions for current teacher");

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var questions = questionRepository.findByCreatedBy(currentUser.getLogin());

        var questionDTOs = questions.stream().map(questionMapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(questionDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getTeacherQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var question = questionRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));

        if (!question.getCreatedBy().getLogin().equals(currentUser.getLogin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the creator of this question");
        }

        return ResponseEntity.ok(questionMapper.toDto(question));
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createTeacherQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);

        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        questionDTO = questionService.save(questionDTO);

        return ResponseEntity.created(new URI("/api/teacher/questions/" + questionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString()))
            .body(questionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateTeacherQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Question : {}, {}", id, questionDTO);

        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var existingQuestion = questionRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));

        if (!existingQuestion.getCreatedBy().getLogin().equals(currentUser.getLogin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the creator of this question");
        }

        questionDTO = questionService.update(questionDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString()))
            .body(questionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacherQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);

        var currentUser = SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var question = questionRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found"));

        if (!question.getCreatedBy().getLogin().equals(currentUser.getLogin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the creator of this question");
        }

        questionService.delete(id);

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
