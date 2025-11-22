import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Card, CardBody, CardTitle, Alert, Spinner, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { getProfile, updateDailyGoal, IStudentProfile } from './profile.service';
import { XpBadge, StreakIndicator, LevelBadge } from 'app/shared/components';

export const StudentProfile: React.FC = () => {
  const [profile, setProfile] = useState<IStudentProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editingGoal, setEditingGoal] = useState(false);
  const [newDailyGoal, setNewDailyGoal] = useState<number>(50);
  const [savingGoal, setSavingGoal] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getProfile();
      setProfile(data);
      setNewDailyGoal(data.dailyGoalXp);
    } catch (err) {
      setError('Erro ao carregar perfil. Por favor, tente novamente.');
      console.error('Error loading profile:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSaveDailyGoal = async () => {
    if (newDailyGoal < 10 || newDailyGoal > 500) {
      setError('A meta diária deve estar entre 10 e 500 XP.');
      return;
    }

    try {
      setSavingGoal(true);
      setError(null);
      const updatedProfile = await updateDailyGoal(newDailyGoal);
      setProfile(updatedProfile);
      setEditingGoal(false);
    } catch (err) {
      setError('Erro ao atualizar meta diária. Por favor, tente novamente.');
      console.error('Error updating daily goal:', err);
    } finally {
      setSavingGoal(false);
    }
  };

  const handleCancelEdit = () => {
    setEditingGoal(false);
    setNewDailyGoal(profile?.dailyGoalXp || 50);
    setError(null);
  };

  if (loading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner color="primary" />
        <p className="mt-3">Carregando perfil...</p>
      </Container>
    );
  }

  if (error && !profile) {
    return (
      <Container className="mt-5">
        <Alert color="danger">{error}</Alert>
        <Button color="primary" onClick={loadProfile}>
          Tentar Novamente
        </Button>
      </Container>
    );
  }

  if (!profile) {
    return null;
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Meu Perfil</h2>
        <Button color="secondary" outline onClick={() => navigate('/student/dashboard')}>
          ← Voltar ao Dashboard
        </Button>
      </div>

      {error && <Alert color="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col md={4}>
          <Card>
            <CardBody className="text-center">
              <div className="mb-3">
                <div
                  style={{
                    width: '100px',
                    height: '100px',
                    borderRadius: '50%',
                    backgroundColor: '#007bff',
                    color: 'white',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    fontSize: '2.5rem',
                    fontWeight: 'bold',
                    margin: '0 auto',
                  }}
                >
                  {profile.firstName?.charAt(0).toUpperCase() || 'U'}
                </div>
              </div>
              <h4>
                {profile.firstName} {profile.lastName}
              </h4>
              <p className="text-muted">{profile.email}</p>
              <p className="text-muted">
                <small>Município: {profile.municipalityCode}</small>
              </p>
            </CardBody>
          </Card>
        </Col>

        <Col md={8}>
          <Card>
            <CardBody>
              <CardTitle tag="h5">Estatísticas Gerais</CardTitle>
              <Row className="mt-3">
                <Col md={4} className="text-center mb-3">
                  <XpBadge xp={profile.totalXp} size="lg" />
                  <p className="mt-2 mb-0">
                    <strong>XP Total</strong>
                  </p>
                </Col>
                <Col md={4} className="text-center mb-3">
                  <LevelBadge level={profile.currentLevel} size="lg" />
                  <p className="mt-2 mb-0">
                    <strong>Nível Atual</strong>
                  </p>
                </Col>
                <Col md={4} className="text-center mb-3">
                  <StreakIndicator streak={profile.currentStreak} size="lg" />
                  <p className="mt-2 mb-0">
                    <strong>Streak Atual</strong>
                  </p>
                </Col>
              </Row>
            </CardBody>
          </Card>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <CardBody>
              <CardTitle tag="h5">Meta Diária de XP</CardTitle>
              {!editingGoal ? (
                <div>
                  <div className="d-flex align-items-center justify-content-between">
                    <div>
                      <h3 className="mb-0">{profile.dailyGoalXp} XP</h3>
                      <small className="text-muted">Meta diária</small>
                    </div>
                    <Button color="primary" outline size="sm" onClick={() => setEditingGoal(true)}>
                      ✏️ Editar
                    </Button>
                  </div>
                </div>
              ) : (
                <Form>
                  <FormGroup>
                    <Label for="dailyGoal">Nova Meta Diária (10-500 XP)</Label>
                    <Input
                      type="number"
                      id="dailyGoal"
                      value={newDailyGoal}
                      onChange={e => setNewDailyGoal(Number(e.target.value))}
                      min={10}
                      max={500}
                      disabled={savingGoal}
                    />
                  </FormGroup>
                  <div className="d-flex gap-2">
                    <Button color="primary" onClick={handleSaveDailyGoal} disabled={savingGoal}>
                      {savingGoal ? <Spinner size="sm" /> : 'Salvar'}
                    </Button>
                    <Button color="secondary" outline onClick={handleCancelEdit} disabled={savingGoal}>
                      Cancelar
                    </Button>
                  </div>
                </Form>
              )}
            </CardBody>
          </Card>
        </Col>

        <Col md={6}>
          <Card>
            <CardBody>
              <CardTitle tag="h5">Estatísticas Detalhadas</CardTitle>
              <div className="mt-3">
                <div className="d-flex justify-content-between mb-2">
                  <span>Lições Completadas:</span>
                  <strong>{profile.statistics.totalLessonsCompleted}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Questões Respondidas:</span>
                  <strong>{profile.statistics.totalQuestionsAnswered}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Respostas Corretas:</span>
                  <strong>{profile.statistics.totalCorrectAnswers}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Taxa de Acerto:</span>
                  <strong>{profile.statistics.accuracyRate.toFixed(1)}%</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Conquistas Desbloqueadas:</span>
                  <strong>{profile.statistics.totalAchievementsUnlocked}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Dias Ativos:</span>
                  <strong>{profile.statistics.totalDaysActive}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Maior Streak:</span>
                  <strong>{profile.statistics.longestStreak} dias</strong>
                </div>
              </div>
            </CardBody>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col>
          <Card>
            <CardBody>
              <CardTitle tag="h5">Atividades Recentes</CardTitle>
              {profile.recentActivities && profile.recentActivities.length > 0 ? (
                <div className="mt-3">
                  {profile.recentActivities.map((activity, index) => (
                    <div
                      key={index}
                      className="d-flex justify-content-between align-items-center p-3 mb-2"
                      style={{
                        backgroundColor: '#f8f9fa',
                        borderRadius: '8px',
                        border: '1px solid #dee2e6',
                      }}
                    >
                      <div>
                        <div>
                          <strong>{activity.type}</strong>
                        </div>
                        <div className="text-muted">
                          <small>{activity.description}</small>
                        </div>
                        <div className="text-muted">
                          <small>{new Date(activity.date).toLocaleDateString('pt-BR')}</small>
                        </div>
                      </div>
                      {activity.xpEarned > 0 && (
                        <div className="text-success">
                          <strong>+{activity.xpEarned} XP</strong>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-muted mt-3">Nenhuma atividade recente.</p>
              )}
            </CardBody>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};
