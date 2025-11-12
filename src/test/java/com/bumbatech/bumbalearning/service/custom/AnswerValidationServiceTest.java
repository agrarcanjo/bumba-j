package com.bumbatech.bumbalearning.service.custom;

import static org.assertj.core.api.Assertions.assertThat;

import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.enumeration.QuestionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnswerValidationServiceTest {

    private AnswerValidationService answerValidationService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        answerValidationService = new AnswerValidationService(objectMapper);
    }

    @Test
    void testValidateMultipleChoice_Correct() {
        Question question = new Question();
        question.setType(QuestionType.MULTIPLE_CHOICE);
        question.setContent("{\"options\": [\"opt1\", \"opt2\", \"opt3\", \"opt4\"], \"correct_index\": 2}");

        String answer = "{\"selected_index\": 2}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isTrue();
    }

    @Test
    void testValidateMultipleChoice_Incorrect() {
        Question question = new Question();
        question.setType(QuestionType.MULTIPLE_CHOICE);
        question.setContent("{\"options\": [\"opt1\", \"opt2\", \"opt3\", \"opt4\"], \"correct_index\": 2}");

        String answer = "{\"selected_index\": 1}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isFalse();
    }

    @Test
    void testValidateFillBlank_Correct() {
        Question question = new Question();
        question.setType(QuestionType.FILL_BLANK);
        question.setContent("{\"sentence\": \"I ___ to school\", \"correct_answers\": [\"go\", \"walk\"]}");

        String answer = "{\"text\": \"go\"}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isTrue();
    }

    @Test
    void testValidateFillBlank_CorrectWithDifferentCase() {
        Question question = new Question();
        question.setType(QuestionType.FILL_BLANK);
        question.setContent("{\"sentence\": \"I ___ to school\", \"correct_answers\": [\"go\", \"walk\"]}");

        String answer = "{\"text\": \"GO\"}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isTrue();
    }

    @Test
    void testValidateFillBlank_Incorrect() {
        Question question = new Question();
        question.setType(QuestionType.FILL_BLANK);
        question.setContent("{\"sentence\": \"I ___ to school\", \"correct_answers\": [\"go\", \"walk\"]}");

        String answer = "{\"text\": \"run\"}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isFalse();
    }

    @Test
    void testValidateListening_Correct() {
        Question question = new Question();
        question.setType(QuestionType.LISTENING);
        question.setContent(
            "{\"audio_url\": \"https://example.com/audio.mp3\", \"question\": \"What did he say?\", \"correct_answer\": \"Hello\"}"
        );

        String answer = "{\"text\": \"hello\"}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isTrue();
    }

    @Test
    void testValidateReading_Correct() {
        Question question = new Question();
        question.setType(QuestionType.READING_COMPREHENSION);
        question.setContent("{\"text\": \"Sample text\", \"question\": \"What is this?\", \"correct_answer\": \"Sample\"}");

        String answer = "{\"text\": \"sample\"}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isTrue();
    }

    @Test
    void testValidateWriting_Similar() {
        Question question = new Question();
        question.setType(QuestionType.WRITING);
        question.setContent("{\"prompt\": \"Describe your day\", \"sample_answer\": \"I went to school and studied\"}");

        String answer = "{\"text\": \"I went to school and studied\"}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isTrue();
    }

    @Test
    void testValidateSpeaking_AlwaysTrue() {
        Question question = new Question();
        question.setType(QuestionType.SPEAKING_PLACEHOLDER);
        question.setContent("{\"prompt\": \"Say: Hello, how are you?\"}");

        String answer = "{\"text\": \"anything\"}";

        boolean result = answerValidationService.validateAnswer(question, answer);

        assertThat(result).isTrue();
    }
}
