package com.bumbatech.bumbalearning.config;

import com.bumbatech.bumbalearning.domain.*;
import com.bumbatech.bumbalearning.domain.enumeration.*;
import com.bumbatech.bumbalearning.repository.*;
import jakarta.annotation.PostConstruct;
import java.time.Instant;
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
    private final LessonQuestionRepository lessonQuestionRepository;
    private final UserRepository userRepository;

    public DataSeeder(
        AchievementRepository achievementRepository,
        TopicRepository topicRepository,
        LessonRepository lessonRepository,
        QuestionRepository questionRepository,
        LessonQuestionRepository lessonQuestionRepository,
        UserRepository userRepository
    ) {
        this.achievementRepository = achievementRepository;
        this.topicRepository = topicRepository;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.lessonQuestionRepository = lessonQuestionRepository;
        this.userRepository = userRepository;
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

        User systemUser = userRepository
            .findOneByLogin("system")
            .orElseGet(() -> {
                User user = userRepository.findOneByLogin("admin").orElse(null);
                if (user == null) {
                    LOG.warn("No system or admin user found. Creating a temporary system user.");
                    user = new User();
                    user.setLogin("system");
                    user.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
                    user.setFirstName("System");
                    user.setLastName("User");
                    user.setEmail("system@localhost");
                    user.setActivated(true);
                    user.setLangKey("pt-br");
                    user = userRepository.save(user);
                }
                return user;
            });

        Topic vocabularyTopic = createTopic("Vocabulário Básico", "Aprenda palavras e expressões essenciais do inglês", Language.ENGLISH);
        topicRepository.save(vocabularyTopic);

        Topic grammarTopic = createTopic("Gramática Fundamental", "Domine as estruturas básicas da língua inglesa", Language.ENGLISH);
        topicRepository.save(grammarTopic);

        Topic listeningTopic = createTopic("Compreensão Auditiva", "Desenvolva sua habilidade de entender inglês falado", Language.ENGLISH);
        topicRepository.save(listeningTopic);

        seedVocabularyLessons(vocabularyTopic, systemUser);
        seedGrammarLessons(grammarTopic, systemUser);
        seedListeningLessons(listeningTopic, systemUser);

        LOG.info("Seeded topics and lessons");
    }

    private Topic createTopic(String name, String description, Language language) {
        Topic topic = new Topic();
        topic.setName(name);
        topic.setDescription(description);
        topic.setLanguage(language);
        return topic;
    }

    private void seedVocabularyLessons(Topic topic, User creator) {
        Lesson lesson1 = createLesson(
            topic,
            creator,
            "Saudações e Apresentações",
            "Aprenda a cumprimentar e se apresentar em inglês",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson2 = createLesson(
            topic,
            creator,
            "Números e Cores",
            "Vocabulário essencial de números e cores",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson3 = createLesson(
            topic,
            creator,
            "Família e Relacionamentos",
            "Palavras para descrever família e relações",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson4 = createLesson(
            topic,
            creator,
            "Comidas e Bebidas",
            "Vocabulário de alimentos e bebidas comuns",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson5 = createLesson(
            topic,
            creator,
            "Animais Domésticos",
            "Nomes de animais de estimação e da fazenda",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );

        lessonRepository.saveAll(List.of(lesson1, lesson2, lesson3, lesson4, lesson5));

        seedVocabularyQuestions(lesson1, topic, creator);
        seedNumbersQuestions(lesson2, topic, creator);
    }

    private void seedGrammarLessons(Topic topic, User creator) {
        Lesson lesson1 = createLesson(
            topic,
            creator,
            "Verbo To Be - Presente",
            "Aprenda o verbo to be no presente",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson2 = createLesson(
            topic,
            creator,
            "Pronomes Pessoais",
            "Domine os pronomes pessoais em inglês",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson3 = createLesson(
            topic,
            creator,
            "Artigos A e An",
            "Quando usar os artigos indefinidos",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson4 = createLesson(
            topic,
            creator,
            "Presente Simples",
            "Estrutura e uso do presente simples",
            DifficultyLevel.INTERMEDIATE,
            Language.ENGLISH
        );
        Lesson lesson5 = createLesson(
            topic,
            creator,
            "Plural dos Substantivos",
            "Regras para formar o plural",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );

        lessonRepository.saveAll(List.of(lesson1, lesson2, lesson3, lesson4, lesson5));

        seedToBeQuestions(lesson1, topic, creator);
    }

    private void seedListeningLessons(Topic topic, User creator) {
        Lesson lesson1 = createLesson(
            topic,
            creator,
            "Conversas Básicas",
            "Pratique ouvir diálogos simples do dia a dia",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson2 = createLesson(
            topic,
            creator,
            "Números e Horários",
            "Compreenda números e expressões de tempo",
            DifficultyLevel.EASY,
            Language.ENGLISH
        );
        Lesson lesson3 = createLesson(
            topic,
            creator,
            "Direções e Localizações",
            "Entenda instruções de direção",
            DifficultyLevel.INTERMEDIATE,
            Language.ENGLISH
        );

        lessonRepository.saveAll(List.of(lesson1, lesson2, lesson3));
    }

    private Lesson createLesson(
        Topic topic,
        User creator,
        String title,
        String description,
        DifficultyLevel difficulty,
        Language language
    ) {
        Lesson lesson = new Lesson();
        lesson.setTopic(topic);
        lesson.setCreatedBy(creator);
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setLevel(difficulty);
        lesson.setLanguage(language);
        lesson.setXpReward(100);
        lesson.setPassThreshold(70);
        lesson.setCreatedAt(Instant.now());
        return lesson;
    }

    private void seedVocabularyQuestions(Lesson lesson, Topic topic, User creator) {
        Question q1 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Como se diz 'Olá' em inglês?",
            "{\"options\":[\"Goodbye\",\"Hello\",\"Thank you\",\"Please\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );
        Question q2 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Qual é a tradução de 'Good morning'?",
            "{\"options\":[\"Boa noite\",\"Bom dia\",\"Boa tarde\",\"Até logo\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );
        Question q3 = createFillBlankQuestion(
            topic,
            creator,
            "Complete: 'My name ___ John'",
            "{\"sentence\":\"My name ___ John\",\"correctAnswer\":\"is\",\"acceptedAnswers\":[\"is\"]}",
            Language.ENGLISH,
            Skill.WRITING,
            DifficultyLevel.EASY
        );
        Question q4 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Como se diz 'Obrigado' em inglês?",
            "{\"options\":[\"Sorry\",\"Please\",\"Thank you\",\"Excuse me\"],\"correctIndex\":2}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );
        Question q5 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Qual é a tradução de 'Goodbye'?",
            "{\"options\":[\"Olá\",\"Tchau/Adeus\",\"Por favor\",\"Desculpe\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );

        questionRepository.saveAll(List.of(q1, q2, q3, q4, q5));

        lessonQuestionRepository.saveAll(
            List.of(
                createLessonQuestion(lesson, q1, 0),
                createLessonQuestion(lesson, q2, 1),
                createLessonQuestion(lesson, q3, 2),
                createLessonQuestion(lesson, q4, 3),
                createLessonQuestion(lesson, q5, 4)
            )
        );
    }

    private void seedNumbersQuestions(Lesson lesson, Topic topic, User creator) {
        Question q1 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Qual é o número 'five' em português?",
            "{\"options\":[\"3\",\"4\",\"5\",\"6\"],\"correctIndex\":2}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );
        Question q2 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Como se escreve o número 10 em inglês?",
            "{\"options\":[\"nine\",\"ten\",\"eleven\",\"twelve\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );
        Question q3 = createFillBlankQuestion(
            topic,
            creator,
            "Complete: 'One, two, ___'",
            "{\"sentence\":\"One, two, ___\",\"correctAnswer\":\"three\",\"acceptedAnswers\":[\"three\",\"3\"]}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );
        Question q4 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Qual é a cor 'red' em português?",
            "{\"options\":[\"Azul\",\"Verde\",\"Vermelho\",\"Amarelo\"],\"correctIndex\":2}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );
        Question q5 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Como se diz 'azul' em inglês?",
            "{\"options\":[\"black\",\"blue\",\"brown\",\"white\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.READING,
            DifficultyLevel.EASY
        );

        questionRepository.saveAll(List.of(q1, q2, q3, q4, q5));

        lessonQuestionRepository.saveAll(
            List.of(
                createLessonQuestion(lesson, q1, 0),
                createLessonQuestion(lesson, q2, 1),
                createLessonQuestion(lesson, q3, 2),
                createLessonQuestion(lesson, q4, 3),
                createLessonQuestion(lesson, q5, 4)
            )
        );
    }

    private void seedToBeQuestions(Lesson lesson, Topic topic, User creator) {
        Question q1 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Complete: 'I ___ a student'",
            "{\"options\":[\"is\",\"am\",\"are\",\"be\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.WRITING,
            DifficultyLevel.EASY
        );
        Question q2 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Complete: 'She ___ happy'",
            "{\"options\":[\"am\",\"is\",\"are\",\"be\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.WRITING,
            DifficultyLevel.EASY
        );
        Question q3 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Complete: 'They ___ friends'",
            "{\"options\":[\"am\",\"is\",\"are\",\"be\"],\"correctIndex\":2}",
            Language.ENGLISH,
            Skill.WRITING,
            DifficultyLevel.EASY
        );
        Question q4 = createFillBlankQuestion(
            topic,
            creator,
            "Complete: 'You ___ welcome'",
            "{\"sentence\":\"You ___ welcome\",\"correctAnswer\":\"are\",\"acceptedAnswers\":[\"are\",\"'re\"]}",
            Language.ENGLISH,
            Skill.WRITING,
            DifficultyLevel.EASY
        );
        Question q5 = createMultipleChoiceQuestion(
            topic,
            creator,
            "Complete: 'He ___ a teacher'",
            "{\"options\":[\"am\",\"is\",\"are\",\"be\"],\"correctIndex\":1}",
            Language.ENGLISH,
            Skill.WRITING,
            DifficultyLevel.EASY
        );

        questionRepository.saveAll(List.of(q1, q2, q3, q4, q5));

        lessonQuestionRepository.saveAll(
            List.of(
                createLessonQuestion(lesson, q1, 0),
                createLessonQuestion(lesson, q2, 1),
                createLessonQuestion(lesson, q3, 2),
                createLessonQuestion(lesson, q4, 3),
                createLessonQuestion(lesson, q5, 4)
            )
        );
    }

    private Question createMultipleChoiceQuestion(
        Topic topic,
        User creator,
        String prompt,
        String content,
        Language language,
        Skill skill,
        DifficultyLevel level
    ) {
        Question question = new Question();
        question.setTopic(topic);
        question.setCreatedBy(creator);
        question.setType(QuestionType.MULTIPLE_CHOICE);
        question.setPrompt(prompt);
        question.setContent(content);
        question.setLanguage(language);
        question.setSkill(skill);
        question.setLevel(level);
        question.setCreatedAt(Instant.now());
        return question;
    }

    private Question createFillBlankQuestion(
        Topic topic,
        User creator,
        String prompt,
        String content,
        Language language,
        Skill skill,
        DifficultyLevel level
    ) {
        Question question = new Question();
        question.setTopic(topic);
        question.setCreatedBy(creator);
        question.setType(QuestionType.FILL_BLANK);
        question.setPrompt(prompt);
        question.setContent(content);
        question.setLanguage(language);
        question.setSkill(skill);
        question.setLevel(level);
        question.setCreatedAt(Instant.now());
        return question;
    }

    private LessonQuestion createLessonQuestion(Lesson lesson, Question question, Integer orderIndex) {
        LessonQuestion lessonQuestion = new LessonQuestion();
        lessonQuestion.setLesson(lesson);
        lessonQuestion.setQuestion(question);
        lessonQuestion.setOrderIndex(orderIndex);
        return lessonQuestion;
    }
}
