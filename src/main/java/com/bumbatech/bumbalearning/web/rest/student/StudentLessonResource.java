package com.bumbatech.bumbalearning.web.rest.student;

import com.bumbatech.bumbalearning.domain.Attempt;
import com.bumbatech.bumbalearning.domain.Lesson;
import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.repository.AttemptRepository;
import com.bumbatech.bumbalearning.repository.LessonRepository;
import com.bumbatech.bumbalearning.repository.QuestionRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.custom.AnswerValidationService;
import com.bumbatech.bumbalearning.service.dto.AttemptDTO;
import com.bumbatech.bumbalearning.service.dto.LessonDTO;
import com.bumbatech.bumbalearning.service.dto.QuestionDTO;
import com.bumbatech.bumbalearning.service.mapper.AttemptMapper;
import com.bumbatech.bumbalearning.service.mapper.LessonMapper;
import com.bumbatech.bumbalearning.service.mapper.QuestionMapper;
import com.bumbatech.bumbalearning.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/lessons")
public class StudentLessonResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentLessonResource.class);

    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final AttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final AnswerValidationService answerValidationService;
    private final LessonMapper lessonMapper;
    private final QuestionMapper questionMapper;
    private final AttemptMapper attemptMapper;

    public StudentLessonResource(
        LessonRepository lessonRepository,
        QuestionRepository questionRepository,
        AttemptRepository attemptRepository,
        UserRepository userRepository,
        AnswerValidationService answerValidationService,
        LessonMapper lessonMapper,
        QuestionMapper questionMapper,
        AttemptMapper attemptMapper
    ) {
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.answerValidationService = answerValidationService;
        this.lessonMapper = lessonMapper;
        this.questionMapper = questionMapper;
        this.attemptMapper = attemptMapper;
    }

    @GetMapping("/next")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<LessonDTO> getNextLesson() {
        LOG.debug("REST request to get next recommended lesson for student");

        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("User not authenticated", "lesson", "notauthenticated"));

        User student = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "lesson", "usernotfound"));

        List<Lesson> allLessons = lessonRepository.findAll();

        if (allLessons.isEmpty()) {
            throw new BadRequestAlertException("No lessons available", "lesson", "nolessons");
        }

        Lesson nextLesson = allLessons.get(0);

        return ResponseEntity.ok(lessonMapper.toDto(nextLesson));
    }

    @GetMapping("/{id}/start")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<Map<String, Object>> startLesson(@PathVariable Long id) {
        LOG.debug("REST request to start lesson: {}", id);

        Lesson lesson = lessonRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Lesson not found", "lesson", "lessonnotfound"));

        List<Question> questions = questionRepository.findByLessonId(id);

        List<QuestionDTO> questionDTOs = questions.stream().map(questionMapper::toDto).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("lesson", lessonMapper.toDto(lesson));
        response.put("questions", questionDTOs);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{lessonId}/questions/{questionId}/answer")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<Map<String, Object>> answerQuestion(
        @PathVariable Long lessonId,
        @PathVariable Long questionId,
        @Valid @RequestBody AnswerRequest answerRequest
    ) {
        LOG.debug("REST request to answer question {} in lesson {}", questionId, lessonId);

        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("User not authenticated", "attempt", "notauthenticated"));

        User student = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "attempt", "usernotfound"));

        Lesson lesson = lessonRepository
            .findById(lessonId)
            .orElseThrow(() -> new BadRequestAlertException("Lesson not found", "attempt", "lessonnotfound"));

        Question question = questionRepository
            .findById(questionId)
            .orElseThrow(() -> new BadRequestAlertException("Question not found", "attempt", "questionnotfound"));

        boolean isCorrect = answerValidationService.validateAnswer(question, answerRequest.getAnswer());

        Attempt attempt = new Attempt();
        attempt.setStudent(student);
        attempt.setQuestion(question);
        attempt.setLesson(lesson);
        attempt.setAnswer(answerRequest.getAnswer());
        attempt.setIsCorrect(isCorrect);
        attempt.setTimeSpentSeconds(answerRequest.getTimeSpentSeconds() != null ? answerRequest.getTimeSpentSeconds() : 0);
        attempt.setAttemptedAt(Instant.now());

        attempt = attemptRepository.save(attempt);

        Map<String, Object> response = new HashMap<>();
        response.put("isCorrect", isCorrect);
        response.put("attempt", attemptMapper.toDto(attempt));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<Map<String, Object>> completeLesson(@PathVariable Long id) {
        LOG.debug("REST request to complete lesson: {}", id);

        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("User not authenticated", "lesson", "notauthenticated"));

        User student = userRepository
            .findOneByLogin(userLogin)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "lesson", "usernotfound"));

        Lesson lesson = lessonRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Lesson not found", "lesson", "lessonnotfound"));

        List<Attempt> attempts = attemptRepository.findByStudentIdAndLessonId(student.getId(), id);

        if (attempts.isEmpty()) {
            throw new BadRequestAlertException("No attempts found for this lesson", "lesson", "noattempts");
        }

        long correctAnswers = attempts.stream().filter(Attempt::getIsCorrect).count();
        int totalQuestions = attempts.size();
        int score = (int) ((correctAnswers * 100.0) / totalQuestions);

        boolean passed = score >= lesson.getPassThreshold();
        int xpEarned = passed ? lesson.getXpReward() : 0;

        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        response.put("correctAnswers", correctAnswers);
        response.put("totalQuestions", totalQuestions);
        response.put("passed", passed);
        response.put("xpEarned", xpEarned);

        return ResponseEntity.ok(response);
    }

    public static class AnswerRequest {

        private String answer;
        private Integer timeSpentSeconds;

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public Integer getTimeSpentSeconds() {
            return timeSpentSeconds;
        }

        public void setTimeSpentSeconds(Integer timeSpentSeconds) {
            this.timeSpentSeconds = timeSpentSeconds;
        }
    }
}
