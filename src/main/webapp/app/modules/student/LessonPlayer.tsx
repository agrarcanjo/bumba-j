import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Card, CardBody, Alert, Spinner, Progress } from 'reactstrap';
import { FeedbackModal, QuestionRenderer, ResultScreen } from 'app/modules/student/components';
import {
  completeLesson,
  IAnswerResponse,
  ILessonComplete,
  ILessonStart,
  startLesson,
  submitAnswer,
} from 'app/modules/student/lesson.service';

export const LessonPlayer: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [lesson, setLesson] = useState<ILessonStart | null>(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [feedback, setFeedback] = useState<IAnswerResponse | null>(null);
  const [showFeedback, setShowFeedback] = useState(false);
  const [result, setResult] = useState<ILessonComplete | null>(null);
  const [showResult, setShowResult] = useState(false);
  const [answers, setAnswers] = useState<Map<number, string>>(new Map());

  useEffect(() => {
    if (id) {
      loadLesson(parseInt(id, 10));
    }
  }, [id]);

  const loadLesson = async (lessonId: number) => {
    try {
      setLoading(true);
      setError(null);
      const data = await startLesson(lessonId);
      setLesson(data);
    } catch (err) {
      setError('Erro ao carregar lição. Por favor, tente novamente.');
      console.error('Error loading lesson:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmitAnswer = async (answer: string) => {
    if (!lesson || submitting) return;

    const currentQuestion = lesson.questions[currentQuestionIndex];
    try {
      setSubmitting(true);
      const response = await submitAnswer(lesson.lessonId, currentQuestion.id, answer);
      setFeedback(response);
      setShowFeedback(true);

      const newAnswers = new Map(answers);
      newAnswers.set(currentQuestion.id, answer);
      setAnswers(newAnswers);
    } catch (err) {
      setError('Erro ao enviar resposta. Por favor, tente novamente.');
      console.error('Error submitting answer:', err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleNext = async () => {
    setShowFeedback(false);
    setFeedback(null);

    if (currentQuestionIndex < lesson!.questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    } else {
      await handleCompleteLesson();
    }
  };

  const handleCompleteLesson = async () => {
    if (!lesson) return;

    try {
      setLoading(true);
      const completionResult = await completeLesson(lesson.lessonId);
      setResult(completionResult);
      setShowResult(true);
    } catch (err) {
      setError('Erro ao finalizar lição. Por favor, tente novamente.');
      console.error('Error completing lesson:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner color="primary" />
        <p className="mt-3">Carregando lição...</p>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-5">
        <Alert color="danger">{error}</Alert>
      </Container>
    );
  }

  if (!lesson) {
    return null;
  }

  const currentQuestion = lesson.questions[currentQuestionIndex];
  const progress = ((currentQuestionIndex + 1) / lesson.totalQuestions) * 100;

  return (
    <Container className="mt-4">
      <div className="mb-4">
        <h3>{lesson.title}</h3>
        <p className="text-muted">{lesson.description}</p>
      </div>

      <div className="mb-3">
        <div className="d-flex justify-content-between mb-2">
          <small className="text-muted">
            Questão {currentQuestionIndex + 1} de {lesson.totalQuestions}
          </small>
          <small className="text-muted">{progress.toFixed(0)}%</small>
        </div>
        <Progress value={progress} color="primary" />
      </div>

      <Card>
        <CardBody>
          <QuestionRenderer question={currentQuestion} onSubmit={handleSubmitAnswer} disabled={submitting || showFeedback} />
        </CardBody>
      </Card>

      {feedback && (
        <FeedbackModal
          isOpen={showFeedback}
          correct={feedback.correct}
          explanation={feedback.explanation}
          correctAnswer={feedback.correctAnswer}
          onNext={handleNext}
        />
      )}

      {result && <ResultScreen isOpen={showResult} result={result} />}
    </Container>
  );
};

export default LessonPlayer;
