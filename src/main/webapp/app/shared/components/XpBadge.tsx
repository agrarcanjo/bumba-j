import React from 'react';
import { Badge } from 'reactstrap';

interface XpBadgeProps {
  xp: number;
  size?: 'sm' | 'md' | 'lg';
}

export const XpBadge: React.FC<XpBadgeProps> = ({ xp, size = 'md' }) => {
  const fontSize = size === 'sm' ? '0.875rem' : size === 'lg' ? '1.25rem' : '1rem';

  return (
    <Badge color="warning" style={{ fontSize, fontWeight: 'bold' }}>
      ⭐ {xp} XP
    </Badge>
  );
};
