import React from 'react';
import { Card, CardBody, Badge, Progress } from 'reactstrap';
import { IStudentAchievement } from '../achievements.service';

interface AchievementCardProps {
  achievement: IStudentAchievement;
  onClick: () => void;
}

export const AchievementCard: React.FC<AchievementCardProps> = ({ achievement, onClick }) => {
  const isUnlocked = achievement.unlocked;
  const progressPercentage = achievement.target > 0 ? Math.min((achievement.current / achievement.target) * 100, 100) : 0;

  return (
    <Card
      className={`achievement-card ${isUnlocked ? 'unlocked' : 'locked'} h-100`}
      style={{
        cursor: 'pointer',
        opacity: isUnlocked ? 1 : 0.6,
        transition: 'all 0.3s ease',
        border: isUnlocked ? '2px solid #28a745' : '1px solid #dee2e6',
      }}
      onClick={onClick}
    >
      <CardBody className="d-flex flex-column align-items-center text-center">
        <div className="mb-3" style={{ fontSize: '3rem', filter: isUnlocked ? 'none' : 'grayscale(100%)' }}>
          {achievement.iconUrl ? (
            <img src={achievement.iconUrl} alt={achievement.name} style={{ width: '80px', height: '80px' }} />
          ) : (
            <span>🏆</span>
          )}
        </div>

        <h5 className="mb-2" style={{ color: isUnlocked ? '#000' : '#6c757d' }}>
          {achievement.name}
        </h5>

        <p className="text-muted small mb-3" style={{ minHeight: '40px' }}>
          {achievement.description}
        </p>

        {isUnlocked ? (
          <Badge color="success" pill className="mb-2">
            ✓ Desbloqueada
          </Badge>
        ) : (
          <div className="w-100">
            <div className="d-flex justify-content-between align-items-center mb-1">
              <small className="text-muted">Progresso</small>
              <small className="text-muted">
                {achievement.current}/{achievement.target}
              </small>
            </div>
            <Progress value={progressPercentage} color="info" className="mb-2" style={{ height: '8px' }} />
          </div>
        )}

        {isUnlocked && achievement.unlockedAt && (
          <small className="text-success">
            {new Date(achievement.unlockedAt).toLocaleDateString('pt-BR', {
              day: '2-digit',
              month: '2-digit',
              year: 'numeric',
            })}
          </small>
        )}
      </CardBody>
    </Card>
  );
};
