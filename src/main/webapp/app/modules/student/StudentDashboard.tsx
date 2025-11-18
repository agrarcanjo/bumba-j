import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Card, CardBody, CardTitle, Alert, Spinner } from 'reactstrap';
import { getDashboard, IDashboardData } from './dashboard.service';
import { XpBadge, StreakIndicator, LevelBadge, ProgressBar } from 'app/shared/components';

export const StudentDashboard: React.FC = () => {
  const [dashboard, setDashboard] = useState<IDashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getDashboard();
      setDashboard(data);
    } catch (err) {
      setError('Erro ao carregar dashboard. Por favor, tente novamente.');
      console.error('Error loading dashboard:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner color="primary" />
        <p className="mt-3">Carregando dashboard...</p>
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

  if (!dashboard) {
    return null;
  }

  return (
    <Container className="mt-4">
      <h2 className="mb-4">Meu Dashboard</h2>

      <Row className="mb-4">
        <Col md={4}>
          <Card>
            <CardBody className="text-center">
              <CardTitle tag="h5">XP Total</CardTitle>
              <XpBadge xp={dashboard.totalXp} size="lg" />
            </CardBody>
          </Card>
        </Col>
        <Col md={4}>
          <Card>
            <CardBody className="text-center">
              <CardTitle tag="h5">Streak Atual</CardTitle>
              <StreakIndicator streak={dashboard.currentStreak} size="lg" />
            </CardBody>
          </Card>
        </Col>
        <Col md={4}>
          <Card>
            <CardBody className="text-center">
              <CardTitle tag="h5">Nível</CardTitle>
              <LevelBadge level={dashboard.level} size="lg" />
            </CardBody>
          </Card>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <CardBody>
              <CardTitle tag="h5">Progresso para Próximo Nível</CardTitle>
              <ProgressBar
                value={dashboard.progressToNextLevel}
                max={100}
                label={`${dashboard.totalXp} / ${dashboard.xpForNextLevel} XP`}
                color="success"
              />
            </CardBody>
          </Card>
        </Col>
        <Col md={6}>
          <Card>
            <CardBody>
              <CardTitle tag="h5">Meta Diária</CardTitle>
              <ProgressBar
                value={dashboard.dailyProgress}
                max={dashboard.dailyGoal}
                label={`${dashboard.dailyProgress} / ${dashboard.dailyGoal} XP`}
                color="info"
              />
            </CardBody>
          </Card>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={12}>
          <Card>
            <CardBody>
              <CardTitle tag="h5">Estatísticas</CardTitle>
              <Row>
                <Col md={3} className="text-center mb-3">
                  <h3 className="text-primary">{dashboard.completedLessons}</h3>
                  <small className="text-muted">Lições Completadas</small>
                </Col>
                <Col md={3} className="text-center mb-3">
                  <h3 className="text-primary">{dashboard.totalQuestions}</h3>
                  <small className="text-muted">Questões Respondidas</small>
                </Col>
                <Col md={3} className="text-center mb-3">
                  <h3 className="text-success">{dashboard.correctAnswers}</h3>
                  <small className="text-muted">Respostas Corretas</small>
                </Col>
                <Col md={3} className="text-center mb-3">
                  <h3 className="text-info">{dashboard.accuracyRate.toFixed(1)}%</h3>
                  <small className="text-muted">Taxa de Acerto</small>
                </Col>
              </Row>
            </CardBody>
          </Card>
        </Col>
      </Row>

      {dashboard.recentAchievements && dashboard.recentAchievements.length > 0 && (
        <Row>
          <Col md={12}>
            <Card>
              <CardBody>
                <CardTitle tag="h5">Conquistas Recentes</CardTitle>
                <Row>
                  {dashboard.recentAchievements.map(achievement => (
                    <Col md={4} key={achievement.id} className="mb-3">
                      <Card className="border-success">
                        <CardBody>
                          <div className="d-flex align-items-center">
                            {achievement.iconUrl && (
                              <img
                                src={achievement.iconUrl}
                                alt={achievement.name}
                                style={{ width: '50px', height: '50px', marginRight: '15px' }}
                              />
                            )}
                            <div>
                              <h6 className="mb-1">{achievement.name}</h6>
                              <small className="text-muted">{achievement.description}</small>
                              <br />
                              <small className="text-success">
                                Desbloqueada em {new Date(achievement.unlockedAt).toLocaleDateString('pt-BR')}
                              </small>
                            </div>
                          </div>
                        </CardBody>
                      </Card>
                    </Col>
                  ))}
                </Row>
              </CardBody>
            </Card>
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default StudentDashboard;
