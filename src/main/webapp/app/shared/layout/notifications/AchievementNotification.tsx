import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTrophy, faTimes } from '@fortawesome/free-solid-svg-icons';
import './achievement-notification.scss';

interface Achievement {
  id: number;
  name: string;
  description: string;
  icon: string;
}

interface AchievementNotificationProps {
  achievement: Achievement | null;
  onClose: () => void;
}

export const AchievementNotification: React.FC<AchievementNotificationProps> = ({ achievement, onClose }) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    if (achievement) {
      setIsVisible(true);
      const timer = setTimeout(() => {
        setIsVisible(false);
        setTimeout(onClose, 300);
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [achievement, onClose]);

  if (!achievement) {
    return null;
  }

  return (
    <div className={`achievement-notification ${isVisible ? 'show' : ''}`}>
      <div className="notification-content">
        <div className="notification-icon">
          <FontAwesomeIcon icon={faTrophy} />
        </div>
        <div className="notification-body">
          <h4>Conquista Desbloqueada!</h4>
          <p className="achievement-name">{achievement.name}</p>
          <p className="achievement-description">{achievement.description}</p>
        </div>
        <button className="close-notification" onClick={() => setIsVisible(false)} aria-label="Fechar notificação">
          <FontAwesomeIcon icon={faTimes} />
        </button>
      </div>
    </div>
  );
};

export default AchievementNotification;
