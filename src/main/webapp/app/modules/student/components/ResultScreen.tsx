import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Card, CardBody, Row, Col, Badge } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { ILessonComplete } from '../lesson.service';

interface ResultScreenProps {
  isOpen: boolean;
  result: ILessonComplete;
}

export const ResultScreen: React.FC<ResultScreenProps> = ({ isOpen, result }) => {
  const navigate = useNavigate();

  const handleBackToDashboard = () => {
    navigate('/student/dashboard');
  };

  const accuracyRate = (result.correctAnswers / result.totalQuestions) * 100;

  return (
    <Modal isOpen={isOpen} centered size="lg" backdrop="static">
      <ModalHeader>🎉 Lição Concluída!</ModalHeader>
      <ModalBody>
        <Card className="mb-3">
          <CardBody>
            <h4 className="text-center mb-4">Seu Desempenho</h4>
            <Row>
              <Col md={4} className="text-center mb-3">
                <h2 className="text-primary">{result.score}</h2>
                <small className="text-muted">Pontuação</small>
              </Col>
              <Col md={4} className="text-center mb-3">
                <h2 className="text-warning">+{result.xpGained} XP</h2>
                <small className="text-muted">XP Ganho</small>
              </Col>
              <Col md={4} className="text-center mb-3">
                <h2 className="text-success">{accuracyRate.toFixed(1)}%</h2>
                <small className="text-muted">Taxa de Acerto</small>
              </Col>
            </Row>
            <Row className="mt-3">
              <Col md={6} className="text-center">
                <p className="mb-0">
                  <strong>Questões Corretas:</strong> {result.correctAnswers} / {result.totalQuestions}
                </p>
              </Col>
              <Col md={6} className="text-center">
                <p className="mb-0">
                  <strong>Questões Erradas:</strong> {result.totalQuestions - result.correctAnswers}
                </p>
              </Col>
            </Row>
          </CardBody>
        </Card>

        {result.achievements && result.achievements.length > 0 && (
          <Card className="border-warning">
            <CardBody>
              <h5 className="mb-3">🏆 Conquistas Desbloqueadas!</h5>
              {result.achievements.map(achievement => (
                <div key={achievement.id} className="d-flex align-items-center mb-3">
                  {achievement.iconUrl && (
                    <img src={achievement.iconUrl} alt={achievement.name} style={{ width: '40px', height: '40px', marginRight: '15px' }} />
                  )}
                  <div>
                    <Badge color="warning" className="mb-1">
                      {achievement.name}
                    </Badge>
                    <p className="mb-0 small text-muted">{achievement.description}</p>
                  </div>
                </div>
              ))}
            </CardBody>
          </Card>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="primary" size="lg" onClick={handleBackToDashboard}>
          Voltar ao Dashboard
        </Button>
      </ModalFooter>
    </Modal>
  );
};
