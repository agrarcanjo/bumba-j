import React from 'react';
import { Badge } from 'reactstrap';

interface LevelBadgeProps {
  level: string;
  size?: 'sm' | 'md' | 'lg';
}

export const LevelBadge: React.FC<LevelBadgeProps> = ({ level, size = 'md' }) => {
  const fontSize = size === 'sm' ? '0.875rem' : size === 'lg' ? '1.25rem' : '1rem';

  const getLevelColor = (lvl: string) => {
    switch (lvl) {
      case 'BEGINNER':
        return 'success';
      case 'INTERMEDIATE':
        return 'info';
      case 'ADVANCED':
        return 'primary';
      default:
        return 'secondary';
    }
  };

  const getLevelLabel = (lvl: string) => {
    switch (lvl) {
      case 'BEGINNER':
        return 'Iniciante';
      case 'INTERMEDIATE':
        return 'Intermediário';
      case 'ADVANCED':
        return 'Avançado';
      default:
        return lvl;
    }
  };

  return (
    <Badge color={getLevelColor(level)} style={{ fontSize, fontWeight: 'bold' }}>
      {getLevelLabel(level)}
    </Badge>
  );
};
