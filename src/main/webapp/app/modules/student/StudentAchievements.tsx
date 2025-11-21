import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Alert, Spinner, Button, ButtonGroup } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { getStudentAchievements, IStudentAchievement } from './achievements.service';
import { AchievementCard, AchievementModal } from './components';

type FilterType = 'all' | 'unlocked' | 'locked';

export const StudentAchievements: React.FC = () => {
  const [achievements, setAchievements] = useState<IStudentAchievement[]>([]);
  const [filteredAchievements, setFilteredAchievements] = useState<IStudentAchievement[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedAchievement, setSelectedAchievement] = useState<IStudentAchievement | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [filter, setFilter] = useState<FilterType>('all');
  const navigate = useNavigate();

  useEffect(() => {
    loadAchievements();
  }, []);

  useEffect(() => {
    applyFilter();
  }, [achievements, filter]);

  const loadAchievements = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getStudentAchievements();
      setAchievements(data);
    } catch (err) {
      setError('Erro ao carregar conquistas. Por favor, tente novamente.');
      console.error('Error loading achievements:', err);
    } finally {
      setLoading(false);
    }
  };

  const applyFilter = () => {
    let filtered = achievements;

    if (filter === 'unlocked') {
      filtered = achievements.filter(a => a.unlocked);
    } else if (filter === 'locked') {
      filtered = achievements.filter(a => !a.unlocked);
    }

    setFilteredAchievements(filtered);
  };

  const handleCardClick = (achievement: IStudentAchievement) => {
    setSelectedAchievement(achievement);
    setModalOpen(true);
  };

  const toggleModal = () => {
    setModalOpen(!modalOpen);
    if (modalOpen) {
      setSelectedAchievement(null);
    }
  };

  const handleFilterChange = (newFilter: FilterType) => {
    setFilter(newFilter);
  };

  const unlockedCount = achievements.filter(a => a.unlocked).length;
  const totalCount = achievements.length;

  if (loading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner color="primary" />
        <p className="mt-3">Carregando conquistas...</p>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-5">
        <Alert color="danger">{error}</Alert>
        <Button color="primary" onClick={loadAchievements}>
          Tentar Novamente
        </Button>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div className="d-flex align-items-center gap-2">
          <Button color="link" onClick={() => navigate('/student/dashboard')} className="p-0 text-decoration-none">
            ← Voltar ao Dashboard
          </Button>
        </div>
        <h2>🏆 Conquistas</h2>
        <div style={{ width: '150px' }}></div>
      </div>

      <Row className="mb-4">
        <Col md={12}>
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h5>
                {unlockedCount} de {totalCount} conquistas desbloqueadas
              </h5>
              <p className="text-muted">Continue praticando para desbloquear mais conquistas!</p>
            </div>
            <ButtonGroup>
              <Button color={filter === 'all' ? 'primary' : 'outline-primary'} onClick={() => handleFilterChange('all')} size="sm">
                Todas ({totalCount})
              </Button>
              <Button
                color={filter === 'unlocked' ? 'success' : 'outline-success'}
                onClick={() => handleFilterChange('unlocked')}
                size="sm"
              >
                Desbloqueadas ({unlockedCount})
              </Button>
              <Button
                color={filter === 'locked' ? 'secondary' : 'outline-secondary'}
                onClick={() => handleFilterChange('locked')}
                size="sm"
              >
                Bloqueadas ({totalCount - unlockedCount})
              </Button>
            </ButtonGroup>
          </div>
        </Col>
      </Row>

      {filteredAchievements.length === 0 ? (
        <Alert color="info">Nenhuma conquista encontrada com o filtro selecionado.</Alert>
      ) : (
        <Row>
          {filteredAchievements.map(achievement => (
            <Col key={achievement.id} xs={12} sm={6} md={4} lg={3} className="mb-4">
              <AchievementCard achievement={achievement} onClick={() => handleCardClick(achievement)} />
            </Col>
          ))}
        </Row>
      )}

      <AchievementModal achievement={selectedAchievement} isOpen={modalOpen} toggle={toggleModal} />
    </Container>
  );
};

export default StudentAchievements;
