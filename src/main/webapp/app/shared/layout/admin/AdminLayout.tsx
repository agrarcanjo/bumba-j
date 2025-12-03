import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import AdminSidebar from '../sidebar/AdminSidebar';
import './admin-layout.scss';

interface AdminLayoutProps {
  children: React.ReactNode;
  onToggleSidebar?: (toggle: () => void) => void;
}

export const AdminLayout: React.FC<AdminLayoutProps> = ({ children, onToggleSidebar }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();
  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = account?.authorities?.includes('ROLE_ADMIN');

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

  if (!isAdmin) {
    return <>{children}</>;
  }

  return (
    <div className="admin-layout">
      <AdminSidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />
      <div className={`admin-content ${sidebarOpen ? 'sidebar-open' : ''}`}>{children}</div>
    </div>
  );
};

export default AdminLayout;
