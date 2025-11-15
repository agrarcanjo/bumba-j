package com.bumbatech.bumbalearning.config;

import com.bumbatech.bumbalearning.domain.*;
import com.bumbatech.bumbalearning.domain.enumeration.*;
import com.bumbatech.bumbalearning.repository.*;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataSeeder {

    private static final Logger LOG = LoggerFactory.getLogger(DataSeeder.class);

    private final AchievementRepository achievementRepository;
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;

    public DataSeeder(
        AchievementRepository achievementRepository,
        TopicRepository topicRepository,
        LessonRepository lessonRepository,
        QuestionRepository questionRepository
    ) {
        this.achievementRepository = achievementRepository;
        this.topicRepository = topicRepository;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    @Transactional
    public void seedData() {
        LOG.info("Starting data seeding...");

        if (achievementRepository.count() == 0) {
            seedAchievements();
        }

        if (topicRepository.count() == 0) {
            seedTopicsAndLessons();
        }

        LOG.info("Data seeding completed!");
    }

    private void seedAchievements() {
        LOG.info("Seeding achievements...");

        List<Achievement> achievements = List.of(
            createAchievement(
                "FIRST_LESSON",
                "Primeira Lição",
                "Completou sua primeira lição",
                "{\"type\":\"lesson_count\",\"value\":1}",
                "/content/images/achievements/first-lesson.png"
            ),
            createAchievement(
                "LESSON_MASTER_10",
                "Mestre das Lições I",
                "Completou 10 lições",
                "{\"type\":\"lesson_count\",\"value\":10}",
                "/content/images/achievements/lesson-master-10.png"
            ),
            createAchievement(
                "LESSON_MASTER_50",
                "Mestre das Lições II",
                "Completou 50 lições",
                "{\"type\":\"lesson_count\",\"value\":50}",
                "/content/images/achievements/lesson-master-50.png"
            ),
            createAchievement(
                "LESSON_MASTER_100",
                "Mestre das Lições III",
                "Completou 100 lições",
                "{\"type\":\"lesson_count\",\"value\":100}",
                "/content/images/achievements/lesson-master-100.png"
            ),
            createAchievement(
                "XP_COLLECTOR_100",
                "Colecionador de XP I",
                "Acumulou 100 XP",
                "{\"type\":\"total_xp\",\"value\":100}",
                "/content/images/achievements/xp-collector-100.png"
            ),
            createAchievement(
                "XP_COLLECTOR_1000",
                "Colecionador de XP II",
                "Acumulou 1000 XP",
                "{\"type\":\"total_xp\",\"value\":1000}",
                "/content/images/achievements/xp-collector-1000.png"
            ),
            createAchievement(
                "XP_COLLECTOR_5000",
                "Colecionador de XP III",
                "Acumulou 5000 XP",
                "{\"type\":\"total_xp\",\"value\":5000}",
                "/content/images/achievements/xp-collector-5000.png"
            ),
            createAchievement(
                "XP_COLLECTOR_10000",
                "Colecionador de XP IV",
                "Acumulou 10000 XP",
                "{\"type\":\"total_xp\",\"value\":10000}",
                "/content/images/achievements/xp-collector-10000.png"
            ),
            createAchievement(
                "STREAK_WARRIOR_7",
                "Guerreiro do Streak I",
                "Manteve 7 dias consecutivos de atividade",
                "{\"type\":\"streak\",\"value\":7}",
                "/content/images/achievements/streak-warrior-7.png"
            ),
            createAchievement(
                "STREAK_WARRIOR_30",
                "Guerreiro do Streak II",
                "Manteve 30 dias consecutivos de atividade",
                "{\"type\":\"streak\",\"value\":30}",
                "/content/images/achievements/streak-warrior-30.png"
            ),
            createAchievement(
                "STREAK_WARRIOR_100",
                "Guerreiro do Streak III",
                "Manteve 100 dias consecutivos de atividade",
                "{\"type\":\"streak\",\"value\":100}",
                "/content/images/achievements/streak-warrior-100.png"
            ),
            createAchievement(
                "PERFECT_SCORE",
                "Pontuação Perfeita",
                "Completou uma lição com 100% de acerto",
                "{\"type\":\"perfect_score\",\"value\":100}",
                "/content/images/achievements/perfect-score.png"
            ),
            createAchievement(
                "SPEED_LEARNER",
                "Aprendiz Veloz",
                "Completou 5 lições em um único dia",
                "{\"type\":\"daily_lessons\",\"value\":5}",
                "/content/images/achievements/speed-learner.png"
            ),
            createAchievement(
                "VOCABULARY_MASTER",
                "Mestre do Vocabulário",
                "Completou 20 lições de vocabulário",
                "{\"type\":\"topic_lessons\",\"topic\":\"VOCABULARY\",\"value\":20}",
                "/content/images/achievements/vocabulary-master.png"
            ),
            createAchievement(
                "GRAMMAR_EXPERT",
                "Expert em Gramática",
                "Completou 20 lições de gramática",
                "{\"type\":\"topic_lessons\",\"topic\":\"GRAMMAR\",\"value\":20}",
                "/content/images/achievements/grammar-expert.png"
            )
        );

        achievementRepository.saveAll(achievements);
        LOG.info("Seeded {} achievements", achievements.size());
    }

    private Achievement createAchievement(String code, String name, String description, String criteria, String iconUrl) {
        Achievement achievement = new Achievement();
        achievement.setCode(code);
        achievement.setName(name);
        achievement.setDescription(description);
        achievement.setCriteria(criteria);
        achievement.setIconUrl(iconUrl);
        return achievement;
    }

    private void seedTopicsAndLessons() {
        LOG.info("Seeding topics and lessons...");

        Topic vocabularyTopic = createTopic(
            "Vocabulário Básico",
            "Aprenda palavras e expressões essenciais do inglês",
            TopicCategory.VOCABULARY,
            1
        );
        topicRepository.save(vocabularyTopic);

        Topic grammarTopic = createTopic(
            "Gramática Fundamental",
            "Domine as estruturas básicas da língua inglesa",
            TopicCategory.GRAMMAR,
            2
        );
        topicRepository.save(grammarTopic);

        Topic listeningTopic = createTopic(
            "Compreensão Auditiva",
            "Desenvolva sua habilidade de entender inglês falado",
            TopicCategory.LISTENING,
            3
        );
        topicRepository.save(listeningTopic);

        seedVocabularyLessons(vocabularyTopic);
        seedGrammarLessons(grammarTopic);
        seedListeningLessons(listeningTopic);

        LOG.info("Seeded topics and lessons");
    }

    private Topic createTopic(String name, String description, TopicCategory category, Integer displayOrder) {
        Topic topic = new Topic();
        topic.setName(name);
        topic.setDescription(description);
        topic.setCategory(category);
        topic.setDisplayOrder(displayOrder);
        return topic;
    }

    private void seedVocabularyLessons(Topic topic) {
        List<Lesson> lessons = List.of(
            createLesson(topic, "Saudações e Apresentações", "Aprenda a cumprimentar e se apresentar em inglês", DifficultyLevel.EASY, 1),
            createLesson(topic, "Números e Cores", "Vocabulário essencial de números e cores", DifficultyLevel.EASY, 2),
            createLesson(topic, "Família e Relacionamentos", "Palavras para descrever família e relações", DifficultyLevel.EASY, 3),
            createLesson(topic, "Comidas e Bebidas", "Vocabulário de alimentos e bebidas comuns", DifficultyLevel.EASY, 4),
            createLesson(topic, "Animais Domésticos", "Nomes de animais de estimação e da fazenda", DifficultyLevel.EASY, 5)
        );

        lessonRepository.saveAll(lessons);

        seedVocabularyQuestions(lessons.get(0));
        seedNumbersQuestions(lessons.get(1));
    }

    private void seedGrammarLessons(Topic topic) {
        List<Lesson> lessons = List.of(
            createLesson(topic, "Verbo To Be - Presente", "Aprenda o verbo to be no presente", DifficultyLevel.EASY, 1),
            createLesson(topic, "Pronomes Pessoais", "Domine os pronomes pessoais em inglês", DifficultyLevel.EASY, 2),
            createLesson(topic, "Artigos A e An", "Quando usar os artigos indefinidos", DifficultyLevel.EASY, 3),
            createLesson(topic, "Presente Simples", "Estrutura e uso do presente simples", DifficultyLevel.INTERMEDIATE, 4),
            createLesson(topic, "Plural dos Substantivos", "Regras para formar o plural", DifficultyLevel.EASY, 5)
        );

        lessonRepository.saveAll(lessons);

        seedToBeQuestions(lessons.get(0));
    }

    private void seedListeningLessons(Topic topic) {
        List<Lesson> lessons = List.of(
            createLesson(topic, "Conversas Básicas", "Pratique ouvir diálogos simples do dia a dia", DifficultyLevel.EASY, 1),
            createLesson(topic, "Números e Horários", "Compreenda números e expressões de tempo", DifficultyLevel.EASY, 2),
            createLesson(topic, "Direções e Localizações", "Entenda instruções de direção", DifficultyLevel.INTERMEDIATE, 3)
        );

        lessonRepository.saveAll(lessons);
    }

    private Lesson createLesson(Topic topic, String title, String description, DifficultyLevel difficulty, Integer displayOrder) {
        Lesson lesson = new Lesson();
        lesson.setTopic(topic);
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setDifficulty(difficulty);
        lesson.setDisplayOrder(displayOrder);
        return lesson;
    }

    private void seedVocabularyQuestions(Lesson lesson) {
        List<Question> questions = List.of(
            createMultipleChoiceQuestion(
                lesson,
                "Como se diz 'Olá' em inglês?",
                "Hello",
                List.of("Goodbye", "Hello", "Thank you", "Please"),
                1,
                1
            ),
            createMultipleChoiceQuestion(
                lesson,
                "Qual é a tradução de 'Good morning'?",
                "Bom dia",
                List.of("Boa noite", "Bom dia", "Boa tarde", "Até logo"),
                1,
                2
            ),
            createFillBlankQuestion(lesson, "Complete: 'My name ___ John'", "is", "My name ___ John", List.of("is", "am"), 3),
            createMultipleChoiceQuestion(
                lesson,
                "Como se diz 'Obrigado' em inglês?",
                "Thank you",
                List.of("Sorry", "Please", "Thank you", "Excuse me"),
                1,
                4
            ),
            createMultipleChoiceQuestion(
                lesson,
                "Qual é a tradução de 'Goodbye'?",
                "Tchau/Adeus",
                List.of("Olá", "Tchau/Adeus", "Por favor", "Desculpe"),
                1,
                5
            )
        );

        questionRepository.saveAll(questions);
    }

    private void seedNumbersQuestions(Lesson lesson) {
        List<Question> questions = List.of(
            createMultipleChoiceQuestion(lesson, "Qual é o número 'five' em português?", "5", List.of("3", "4", "5", "6"), 1, 1),
            createMultipleChoiceQuestion(
                lesson,
                "Como se escreve o número 10 em inglês?",
                "ten",
                List.of("nine", "ten", "eleven", "twelve"),
                1,
                2
            ),
            createFillBlankQuestion(lesson, "Complete: 'One, two, ___'", "three", "One, two, ___", List.of("three", "3"), 3),
            createMultipleChoiceQuestion(
                lesson,
                "Qual é a cor 'red' em português?",
                "Vermelho",
                List.of("Azul", "Verde", "Vermelho", "Amarelo"),
                1,
                4
            ),
            createMultipleChoiceQuestion(lesson, "Como se diz 'azul' em inglês?", "blue", List.of("black", "blue", "brown", "white"), 1, 5)
        );

        questionRepository.saveAll(questions);
    }

    private void seedToBeQuestions(Lesson lesson) {
        List<Question> questions = List.of(
            createMultipleChoiceQuestion(lesson, "Complete: 'I ___ a student'", "am", List.of("is", "am", "are", "be"), 1, 1),
            createMultipleChoiceQuestion(lesson, "Complete: 'She ___ happy'", "is", List.of("am", "is", "are", "be"), 1, 2),
            createMultipleChoiceQuestion(lesson, "Complete: 'They ___ friends'", "are", List.of("am", "is", "are", "be"), 1, 3),
            createFillBlankQuestion(lesson, "Complete: 'You ___ welcome'", "are", "You ___ welcome", List.of("are", "'re"), 4),
            createMultipleChoiceQuestion(lesson, "Complete: 'He ___ a teacher'", "is", List.of("am", "is", "are", "be"), 1, 5)
        );

        questionRepository.saveAll(questions);
    }

    private Question createMultipleChoiceQuestion(
        Lesson lesson,
        String questionText,
        String correctAnswer,
        List<String> options,
        Integer points,
        Integer displayOrder
    ) {
        Question question = new Question();
        question.setLesson(lesson);
        question.setQuestionType(QuestionType.MULTIPLE_CHOICE);
        question.setQuestionText(questionText);
        question.setCorrectAnswer(correctAnswer);
        question.setOptions(String.join("|", options));
        question.setPoints(points);
        question.setDisplayOrder(displayOrder);
        return question;
    }

    private Question createFillBlankQuestion(
        Lesson lesson,
        String questionText,
        String correctAnswer,
        String blankText,
        List<String> acceptedAnswers,
        Integer displayOrder
    ) {
        Question question = new Question();
        question.setLesson(lesson);
        question.setQuestionType(QuestionType.FILL_BLANK);
        question.setQuestionText(questionText);
        question.setCorrectAnswer(correctAnswer);
        question.setBlankText(blankText);
        question.setAcceptedAnswers(String.join("|", acceptedAnswers));
        question.setPoints(1);
        question.setDisplayOrder(displayOrder);
        return question;
    }
}
