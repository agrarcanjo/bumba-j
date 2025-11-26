import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col, Card, CardBody, Badge, Spinner, Alert } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBook, faClock, faStar } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import './lessons.scss';

interface Lesson {
  id: number;
  title: string;
  description: string;
  difficulty: string;
  estimatedTime: number;
  topicName: string;
  completed: boolean;
}

export const Lessons: React.FC = () => {
  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadLessons();
  }, []);

  const loadLessons = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await axios.get<Lesson[]>('/api/lessons');
      setLessons(response.data);
    } catch (err) {
      setError('Erro ao carregar lições. Por favor, tente novamente.');
      console.error('Error loading lessons:', err);
    } finally {
      setLoading(false);
    }
  };

  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'EASY':
        return 'success';
      case 'INTERMEDIATE':
        return 'warning';
      case 'ADVANCED':
        return 'danger';
      default:
        return 'secondary';
    }
  };

  const getDifficultyLabel = (difficulty: string) => {
    switch (difficulty) {
      case 'EASY':
        return 'Fácil';
      case 'INTERMEDIATE':
        return 'Intermediário';
      case 'ADVANCED':
        return 'Avançado';
      default:
        return difficulty;
    }
  };

  if (loading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner color="primary" />
        <p className="mt-3">Carregando lições...</p>
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

  return (
    <Container className="lessons-page mt-4">
      <div className="page-header mb-4">
        <h2>
          <FontAwesomeIcon icon={faBook} className="me-2" />
          Lições Disponíveis
        </h2>
        <p className="text-muted">Escolha uma lição para começar a aprender</p>
      </div>

      <Row>
        {lessons.map(lesson => (
          <Col key={lesson.id} md={6} lg={4} className="mb-4">
            <Card className={`lesson-card ${lesson.completed ? 'completed' : ''}`}>
              <CardBody>
                <div className="lesson-header">
                  <Badge color={getDifficultyColor(lesson.difficulty)} className="mb-2">
                    {getDifficultyLabel(lesson.difficulty)}
                  </Badge>
                  {lesson.completed && (
                    <Badge color="success" className="mb-2 ms-2">
                      Concluída
                    </Badge>
                  )}
                </div>
                <h5 className="lesson-title">{lesson.title}</h5>
                <p className="lesson-description text-muted">{lesson.description}</p>
                <div className="lesson-meta">
                  <span className="meta-item">
                    <FontAwesomeIcon icon={faClock} className="me-1" />
                    {lesson.estimatedTime} min
                  </span>
                  <span className="meta-item">
                    <FontAwesomeIcon icon={faStar} className="me-1" />
                    {lesson.topicName}
                  </span>
                </div>
                <Link to={`/student/lesson/${lesson.id}`} className="btn btn-primary w-100 mt-3">
                  {lesson.completed ? 'Revisar Lição' : 'Começar Lição'}
                </Link>
              </CardBody>
            </Card>
          </Col>
        ))}
      </Row>

      {lessons.length === 0 && (
        <Alert color="info" className="text-center">
          Nenhuma lição disponível no momento.
        </Alert>
      )}
    </Container>
  );
};

export default Lessons;
