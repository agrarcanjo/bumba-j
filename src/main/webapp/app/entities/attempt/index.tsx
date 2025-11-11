import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Attempt from './attempt';
import AttemptDetail from './attempt-detail';
import AttemptUpdate from './attempt-update';
import AttemptDeleteDialog from './attempt-delete-dialog';

const AttemptRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Attempt />} />
    <Route path="new" element={<AttemptUpdate />} />
    <Route path=":id">
      <Route index element={<AttemptDetail />} />
      <Route path="edit" element={<AttemptUpdate />} />
      <Route path="delete" element={<AttemptDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AttemptRoutes;
