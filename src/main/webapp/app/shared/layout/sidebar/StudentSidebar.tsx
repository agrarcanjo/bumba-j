import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTachometerAlt, faBook, faTrophy, faMedal, faUser, faTimes, faBars } from '@fortawesome/free-solid-svg-icons';
import './student-sidebar.scss';

interface StudentSidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

const menuItems = [
  { path: '/student/dashboard', icon: faTachometerAlt, label: 'Dashboard' },
  { path: '/student/lessons', icon: faBook, label: 'Lições' },
  { path: '/student/ranking', icon: faTrophy, label: 'Ranking' },
  { path: '/student/achievements', icon: faMedal, label: 'Conquistas' },
  { path: '/student/profile', icon: faUser, label: 'Perfil' },
];

export const StudentSidebar: React.FC<StudentSidebarProps> = ({ isOpen, onClose }) => {
  const location = useLocation();

  return (
    <>
      <div className={`sidebar-overlay ${isOpen ? 'active' : ''}`} onClick={onClose} />
      <aside className={`student-sidebar ${isOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <h3>Menu do Aluno</h3>
          <button className="close-btn" onClick={onClose} aria-label="Fechar menu">
            <FontAwesomeIcon icon={faTimes} />
          </button>
        </div>
        <nav className="sidebar-nav">
          <ul>
            {menuItems.map(item => (
              <li key={item.path}>
                <Link to={item.path} className={location.pathname === item.path ? 'active' : ''} onClick={onClose}>
                  <FontAwesomeIcon icon={item.icon} className="menu-icon" />
                  <span>{item.label}</span>
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </aside>
    </>
  );
};

export default StudentSidebar;
