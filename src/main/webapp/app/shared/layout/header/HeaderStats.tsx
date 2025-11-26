import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar, faFire } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import './header-stats.scss';

interface HeaderStatsData {
  totalXp: number;
  currentStreak: number;
  level: string;
}

export const HeaderStats = () => {
  const [stats, setStats] = useState<HeaderStatsData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await axios.get<any>('/api/student/dashboard');
        setStats({
          totalXp: response.data.totalXp,
          currentStreak: response.data.currentStreak,
          level: response.data.level,
        });
      } catch (error) {
        console.error('Error fetching header stats:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading || !stats) {
    return null;
  }

  return (
    <div className="header-stats">
      <div className="stat-item xp-badge" title={`Nível: ${stats.level}`}>
        <FontAwesomeIcon icon={faStar} className="stat-icon" />
        <span className="stat-value">{stats.totalXp}</span>
      </div>
      <div className="stat-item streak-badge" title={`Dias consecutivos: ${stats.currentStreak}`}>
        <FontAwesomeIcon icon={faFire} className="stat-icon" />
        <span className="stat-value">{stats.currentStreak}</span>
      </div>
    </div>
  );
};

export default HeaderStats;
