import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Card, CardBody, CardTitle, Alert, Spinner, Table, ButtonGroup, Button, Badge } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { getRanking, IRankingData, RankingPeriod } from './ranking.service';
import { XpBadge, StreakIndicator, LevelBadge } from 'app/shared/components';

export const StudentRanking: React.FC = () => {
  const navigate = useNavigate();
  const [ranking, setRanking] = useState<IRankingData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedPeriod, setSelectedPeriod] = useState<RankingPeriod>('all');

  useEffect(() => {
    loadRanking();
  }, [selectedPeriod]);

  const loadRanking = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getRanking(selectedPeriod);
      setRanking(data);
    } catch (err) {
      setError('Erro ao carregar ranking. Por favor, tente novamente.');
      console.error('Error loading ranking:', err);
    } finally {
      setLoading(false);
    }
  };

  const handlePeriodChange = (period: RankingPeriod) => {
    setSelectedPeriod(period);
  };

  const formatLastUpdated = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  if (loading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner color="primary" />
        <p className="mt-3">Carregando ranking...</p>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-5">
        <Alert color="danger">{error}</Alert>
        <Button color="primary" onClick={loadRanking}>
          Tentar Novamente
        </Button>
      </Container>
    );
  }

  if (!ranking) {
    return null;
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div className="d-flex align-items-center gap-2">
          <Button color="link" onClick={() => navigate('/student/dashboard')} className="p-0 text-decoration-none">
            ← Voltar ao Dashboard
          </Button>
        </div>
        <h2>🏆 Ranking de Alunos</h2>
        <div style={{ width: '150px' }}></div>
      </div>

      <Row className="mb-4">
        <Col md={12}>
          <Card>
            <CardBody>
              <div className="d-flex justify-content-between align-items-center mb-3">
                <div>
                  <CardTitle tag="h5">Filtrar por Período</CardTitle>
                </div>
                <ButtonGroup>
                  <Button
                    color={selectedPeriod === 'week' ? 'primary' : 'outline-primary'}
                    onClick={() => handlePeriodChange('week')}
                    size="sm"
                  >
                    Semana
                  </Button>
                  <Button
                    color={selectedPeriod === 'month' ? 'primary' : 'outline-primary'}
                    onClick={() => handlePeriodChange('month')}
                    size="sm"
                  >
                    Mês
                  </Button>
                  <Button
                    color={selectedPeriod === 'all' ? 'primary' : 'outline-primary'}
                    onClick={() => handlePeriodChange('all')}
                    size="sm"
                  >
                    Todos
                  </Button>
                </ButtonGroup>
              </div>

              <div className="mb-3 text-muted small">
                <div>Total de participantes: {ranking.totalUsers}</div>
                <div>Última atualização: </div>
              </div>
            </CardBody>
          </Card>
        </Col>
      </Row>

      <Row>
        <Col md={12}>
          <Card>
            <CardBody>
              {ranking.rankings && ranking.rankings.length === 0 ? (
                <Alert color="info">Nenhum dado de ranking disponível para o período selecionado.</Alert>
              ) : (
                <div className="table-responsive">
                  <Table hover>
                    <thead>
                      <tr>
                        <th className="text-center" style={{ width: '80px' }}>
                          Posição
                        </th>
                        <th>Nome</th>
                        <th className="text-center">XP</th>
                        <th className="text-center">Nível</th>
                        <th className="text-center">Streak</th>
                      </tr>
                    </thead>
                    <tbody>
                      {ranking.rankings.map(entry => (
                        <tr
                          key={entry.userId}
                          className={entry.isCurrentUser ? 'table-primary' : ''}
                          style={entry.isCurrentUser ? { fontWeight: 'bold' } : {}}
                        >
                          <td className="text-center">
                            {entry.rank <= 3 ? (
                              <Badge color={entry.rank === 1 ? 'warning' : entry.rank === 2 ? 'secondary' : 'danger'} pill>
                                {entry.rank === 1 ? '🥇' : entry.rank === 2 ? '🥈' : '🥉'} {entry.rank}
                              </Badge>
                            ) : (
                              <span className="text-muted">#{entry.rank}</span>
                            )}
                          </td>
                          <td>
                            {entry.userName}
                            {entry.isCurrentUser && (
                              <Badge color="info" className="ms-2" pill>
                                Você
                              </Badge>
                            )}
                          </td>
                          <td className="text-center">
                            <XpBadge xp={entry.totalXp} size="sm" />
                          </td>
                          <td className="text-center">
                            <LevelBadge level={entry.currentLevel} size="sm" />
                          </td>
                          <td className="text-center">
                            <StreakIndicator streak={entry.currentStreak} size="sm" />
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                </div>
              )}
            </CardBody>
          </Card>
        </Col>
      </Row>

      {ranking.currentUserRank > 50 && (
        <Row className="mt-3">
          <Col md={12}>
            <Alert color="info">
              <strong>Sua posição atual: #{ranking.currentUserRank}</strong>
              <div className="small mt-1">Continue praticando para subir no ranking!</div>
            </Alert>
          </Col>
        </Row>
      )}
    </Container>
  );
};
