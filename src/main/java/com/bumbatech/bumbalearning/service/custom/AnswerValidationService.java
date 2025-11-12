package com.bumbatech.bumbalearning.service.custom;

import com.bumbatech.bumbalearning.domain.Question;
import com.bumbatech.bumbalearning.domain.enumeration.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AnswerValidationService {

    private static final Logger log = LoggerFactory.getLogger(AnswerValidationService.class);
    private final ObjectMapper objectMapper;

    public AnswerValidationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean validateAnswer(Question question, String answerJson) {
        try {
            QuestionType type = question.getType();

            switch (type) {
                case MULTIPLE_CHOICE:
                    return validateMultipleChoice(question, answerJson);
                case FILL_BLANK:
                    return validateFillBlank(question, answerJson);
                case LISTENING:
                    return validateListening(question, answerJson);
                case READING_COMPREHENSION:
                    return validateReading(question, answerJson);
                case WRITING:
                    return validateWriting(question, answerJson);
                case SPEAKING_PLACEHOLDER:
                    return validateSpeaking(question, answerJson);
                default:
                    log.warn("Unknown question type: {}", type);
                    return false;
            }
        } catch (Exception e) {
            log.error("Error validating answer for question {}: {}", question.getId(), e.getMessage());
            return false;
        }
    }

    private boolean validateMultipleChoice(Question question, String answerJson) throws Exception {
        JsonNode questionContent = objectMapper.readTree(question.getContent());
        JsonNode studentAnswer = objectMapper.readTree(answerJson);

        int correctIndex = questionContent.get("correct_index").asInt();
        int selectedIndex = studentAnswer.get("selected_index").asInt();

        return correctIndex == selectedIndex;
    }

    private boolean validateFillBlank(Question question, String answerJson) throws Exception {
        JsonNode questionContent = objectMapper.readTree(question.getContent());
        JsonNode studentAnswer = objectMapper.readTree(answerJson);

        String studentText = studentAnswer.get("text").asText().trim().toLowerCase();

        JsonNode correctAnswersNode = questionContent.get("correct_answers");
        if (correctAnswersNode.isArray()) {
            for (JsonNode correctAnswer : correctAnswersNode) {
                String correct = correctAnswer.asText().trim().toLowerCase();
                if (studentText.equals(correct)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean validateListening(Question question, String answerJson) throws Exception {
        JsonNode questionContent = objectMapper.readTree(question.getContent());
        JsonNode studentAnswer = objectMapper.readTree(answerJson);

        String correctAnswer = questionContent.get("correct_answer").asText().trim().toLowerCase();
        String studentText = studentAnswer.get("text").asText().trim().toLowerCase();

        return correctAnswer.equals(studentText);
    }

    private boolean validateReading(Question question, String answerJson) throws Exception {
        JsonNode questionContent = objectMapper.readTree(question.getContent());
        JsonNode studentAnswer = objectMapper.readTree(answerJson);

        String correctAnswer = questionContent.get("correct_answer").asText().trim().toLowerCase();
        String studentText = studentAnswer.get("text").asText().trim().toLowerCase();

        return correctAnswer.equals(studentText);
    }

    private boolean validateWriting(Question question, String answerJson) throws Exception {
        JsonNode questionContent = objectMapper.readTree(question.getContent());
        JsonNode studentAnswer = objectMapper.readTree(answerJson);

        String sampleAnswer = questionContent.get("sample_answer").asText().trim().toLowerCase();
        String studentText = studentAnswer.get("text").asText().trim().toLowerCase();

        return calculateSimilarity(sampleAnswer, studentText) >= 0.7;
    }

    private boolean validateSpeaking(Question question, String answerJson) {
        return true;
    }

    private double calculateSimilarity(String text1, String text2) {
        if (text1.equals(text2)) {
            return 1.0;
        }

        String[] words1 = text1.split("\\s+");
        String[] words2 = text2.split("\\s+");

        int matchingWords = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.equals(word2)) {
                    matchingWords++;
                    break;
                }
            }
        }

        int totalWords = Math.max(words1.length, words2.length);
        return totalWords > 0 ? (double) matchingWords / totalWords : 0.0;
    }
}
