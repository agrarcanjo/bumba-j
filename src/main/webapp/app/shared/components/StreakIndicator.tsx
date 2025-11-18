import React from 'react';
import { Badge } from 'reactstrap';

interface StreakIndicatorProps {
  streak: number;
  size?: 'sm' | 'md' | 'lg';
}

export const StreakIndicator: React.FC<StreakIndicatorProps> = ({ streak, size = 'md' }) => {
  const fontSize = size === 'sm' ? '0.875rem' : size === 'lg' ? '1.25rem' : '1rem';

  return (
    <Badge color="danger" style={{ fontSize, fontWeight: 'bold' }}>
      🔥 {streak} dias
    </Badge>
  );
};
