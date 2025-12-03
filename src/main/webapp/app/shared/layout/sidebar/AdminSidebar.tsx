import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBook, faChartLine, faCog, faTachometerAlt, faTimes, faUsers } from '@fortawesome/free-solid-svg-icons';
import './admin-sidebar.scss';

interface AdminSidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

const menuItems = [
  { path: '/admin/dashboard', icon: faTachometerAlt, label: 'Dashboard' },
  { path: '/admin/dashboard/users', icon: faUsers, label: 'Usuários' },
  { path: '/admin/dashboard/content', icon: faBook, label: 'Conteúdo' },
  { path: '/admin/metrics', icon: faChartLine, label: 'Métricas' },
  { path: '/admin/user-management', icon: faCog, label: 'Administração' },
];

export const AdminSidebar: React.FC<AdminSidebarProps> = ({ isOpen, onClose }) => {
  const location = useLocation();

  return (
    <>
      <div className={`sidebar-overlay ${isOpen ? 'active' : ''}`} onClick={onClose} />
      <aside className={`admin-sidebar ${isOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <h3>Menu do Admin</h3>
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

export default AdminSidebar;
