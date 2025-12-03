import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import TeacherSidebar from '../sidebar/TeacherSidebar';
import './teacher-layout.scss';

interface TeacherLayoutProps {
  children: React.ReactNode;
  onToggleSidebar?: (toggle: () => void) => void;
}

export const TeacherLayout: React.FC<TeacherLayoutProps> = ({ children, onToggleSidebar }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();
  const account = useAppSelector(state => state.authentication.account);
  const isTeacher = account?.authorities?.includes('ROLE_TEACHER');

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

  if (!isTeacher) {
    return <>{children}</>;
  }

  return (
    <div className="teacher-layout">
      <TeacherSidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      <div className={`teacher-content ${sidebarOpen ? 'sidebar-open' : ''}`}>{children}</div>
    </div>
  );
};

export default TeacherLayout;
