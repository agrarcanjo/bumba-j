import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LessonAssignment from './lesson-assignment';
import LessonAssignmentDetail from './lesson-assignment-detail';
import LessonAssignmentUpdate from './lesson-assignment-update';
import LessonAssignmentDeleteDialog from './lesson-assignment-delete-dialog';

const LessonAssignmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LessonAssignment />} />
    <Route path="new" element={<LessonAssignmentUpdate />} />
    <Route path=":id">
      <Route index element={<LessonAssignmentDetail />} />
      <Route path="edit" element={<LessonAssignmentUpdate />} />
      <Route path="delete" element={<LessonAssignmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LessonAssignmentRoutes;
