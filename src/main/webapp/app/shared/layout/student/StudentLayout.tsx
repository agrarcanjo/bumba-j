import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import StudentSidebar from '../sidebar/StudentSidebar';
import AchievementNotification from '../notifications/AchievementNotification';
import './student-layout.scss';

interface StudentLayoutProps {
  children: React.ReactNode;
  onToggleSidebar?: (toggle: () => void) => void;
}

export const StudentLayout: React.FC<StudentLayoutProps> = ({ children, onToggleSidebar }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [currentAchievement, setCurrentAchievement] = useState(null);
  const location = useLocation();
  const account = useAppSelector(state => state.authentication.account);
  const isStudent = account?.authorities?.includes('ROLE_STUDENT');

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  useEffect(() => {
    if (onToggleSidebar) {
      onToggleSidebar(toggleSidebar);
    }
  }, [onToggleSidebar]);

  useEffect(() => {
    setSidebarOpen(false);
  }, [location.pathname]);

  useEffect(() => {
    const handleAchievementUnlocked = (event: CustomEvent) => {
      setCurrentAchievement(event.detail);
    };

    window.addEventListener('achievement-unlocked', handleAchievementUnlocked as EventListener);

    return () => {
      window.removeEventListener('achievement-unlocked', handleAchievementUnlocked as EventListener);
    };
  }, []);

  const handleCloseNotification = () => {
    setCurrentAchievement(null);
  };

  if (!isStudent) {
    return <>{children}</>;
  }

  return (
    <div className="student-layout">
      <StudentSidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      <div className={`student-content ${sidebarOpen ? 'sidebar-open' : ''}`}>{children}</div>
      <AchievementNotification achievement={currentAchievement} onClose={handleCloseNotification} />
    </div>
  );
};

export default StudentLayout;
