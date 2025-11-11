import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Recommendation from './recommendation';
import RecommendationDetail from './recommendation-detail';
import RecommendationUpdate from './recommendation-update';
import RecommendationDeleteDialog from './recommendation-delete-dialog';

const RecommendationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Recommendation />} />
    <Route path="new" element={<RecommendationUpdate />} />
    <Route path=":id">
      <Route index element={<RecommendationDetail />} />
      <Route path="edit" element={<RecommendationUpdate />} />
      <Route path="delete" element={<RecommendationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RecommendationRoutes;
