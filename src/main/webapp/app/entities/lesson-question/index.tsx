import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LessonQuestion from './lesson-question';
import LessonQuestionDetail from './lesson-question-detail';
import LessonQuestionUpdate from './lesson-question-update';
import LessonQuestionDeleteDialog from './lesson-question-delete-dialog';

const LessonQuestionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LessonQuestion />} />
    <Route path="new" element={<LessonQuestionUpdate />} />
    <Route path=":id">
      <Route index element={<LessonQuestionDetail />} />
      <Route path="edit" element={<LessonQuestionUpdate />} />
      <Route path="delete" element={<LessonQuestionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LessonQuestionRoutes;
