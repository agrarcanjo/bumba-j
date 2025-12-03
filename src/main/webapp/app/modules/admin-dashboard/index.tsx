import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import AdminDashboard from './admin-dashboard';
import AdminUsers from './admin-users';
import AdminContent from './admin-content';
import { Route } from 'react-router-dom';

const AdminDashboardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AdminDashboard />} />
    <Route path="users" element={<AdminUsers />} />
    <Route path="content" element={<AdminContent />} />
  </ErrorBoundaryRoutes>
);

export default AdminDashboardRoutes;
