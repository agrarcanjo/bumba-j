import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBook, faChalkboardTeacher, faClipboardList, faTachometerAlt, faTimes, faUser } from '@fortawesome/free-solid-svg-icons';
import './teacher-sidebar.scss';

interface TeacherSidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

const menuItems = [
  { path: '/teacher/dashboard', icon: faTachometerAlt, label: 'Dashboard' },
  { path: '/teacher/classes', icon: faChalkboardTeacher, label: 'Turmas' },
  { path: '/teacher/assignments', icon: faClipboardList, label: 'Atribuições' },
  { path: '/teacher/questions', icon: faBook, label: 'Questões' },
  { path: '/account', icon: faUser, label: 'Perfil' },
];

export const TeacherSidebar: React.FC<TeacherSidebarProps> = ({ isOpen, onClose }) => {
  const location = useLocation();

  return (
    <>
      <div className={`sidebar-overlay ${isOpen ? 'active' : ''}`} onClick={onClose} />
      <aside className={`teacher-sidebar ${isOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <h3>Menu do Professor</h3>
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

export default TeacherSidebar;
