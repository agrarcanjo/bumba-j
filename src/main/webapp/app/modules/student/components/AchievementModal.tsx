import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Badge, Progress } from 'reactstrap';
import { IStudentAchievement } from 'app/modules/student';

interface AchievementModalProps {
  achievement: IStudentAchievement | null;
  isOpen: boolean;
  toggle: () => void;
}

export const AchievementModal: React.FC<AchievementModalProps> = ({ achievement, isOpen, toggle }) => {
  if (!achievement) return null;

  const isUnlocked = achievement.unlocked;
  const progressPercentage = achievement.target > 0 ? Math.min((achievement.current / achievement.target) * 100, 100) : 0;

  return (
    <Modal isOpen={isOpen} toggle={toggle} centered size="lg">
      <ModalHeader toggle={toggle}>{achievement.name}</ModalHeader>
      <ModalBody>
        <div className="text-center mb-4">
          <div style={{ fontSize: '5rem', filter: isUnlocked ? 'none' : 'grayscale(100%)' }}>
            {achievement.iconUrl ? (
              <img src={achievement.iconUrl} alt={achievement.name} style={{ width: '120px', height: '120px' }} />
            ) : (
              <span>🏆</span>
            )}
          </div>
        </div>

        <div className="mb-4">
          <h5>Descrição</h5>
          <p className="text-muted">{achievement.description}</p>
        </div>

        {isUnlocked ? (
          <div className="text-center">
            <Badge color="success" pill style={{ fontSize: '1.2rem', padding: '10px 20px' }}>
              ✓ Conquista Desbloqueada
            </Badge>
            {achievement.unlockedAt && (
              <p className="text-success mt-3">
                Desbloqueada em{' '}
                {new Date(achievement.unlockedAt).toLocaleDateString('pt-BR', {
                  day: '2-digit',
                  month: '2-digit',
                  year: 'numeric',
                  hour: '2-digit',
                  minute: '2-digit',
                })}
              </p>
            )}
          </div>
        ) : (
          <div>
            <h5>Progresso</h5>
            <div className="d-flex justify-content-between align-items-center mb-2">
              <span>
                {achievement.current} / {achievement.target}
              </span>
              <span className="text-muted">{progressPercentage.toFixed(0)}%</span>
            </div>
            <Progress value={progressPercentage} color="info" style={{ height: '20px' }}>
              {progressPercentage.toFixed(0)}%
            </Progress>
            <p className="text-muted mt-3">
              Continue praticando para desbloquear esta conquista! Faltam apenas {achievement.target - achievement.current} para completar.
            </p>
          </div>
        )}
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>
          Fechar
        </Button>
      </ModalFooter>
    </Modal>
  );
};
