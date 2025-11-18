import React from 'react';
import { Progress } from 'reactstrap';

interface ProgressBarProps {
  value: number;
  max?: number;
  label?: string;
  color?: string;
  showPercentage?: boolean;
}

export const ProgressBar: React.FC<ProgressBarProps> = ({ value, max = 100, label, color = 'primary', showPercentage = true }) => {
  const percentage = Math.round((value / max) * 100);

  return (
    <div className="mb-3">
      {label && (
        <div className="d-flex justify-content-between mb-1">
          <small className="text-muted">{label}</small>
          {showPercentage && <small className="text-muted">{percentage}%</small>}
        </div>
      )}
      <Progress value={percentage} color={color} />
    </div>
  );
};
