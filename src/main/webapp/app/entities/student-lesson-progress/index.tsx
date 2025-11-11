import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudentLessonProgress from './student-lesson-progress';
import StudentLessonProgressDetail from './student-lesson-progress-detail';
import StudentLessonProgressUpdate from './student-lesson-progress-update';
import StudentLessonProgressDeleteDialog from './student-lesson-progress-delete-dialog';

const StudentLessonProgressRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudentLessonProgress />} />
    <Route path="new" element={<StudentLessonProgressUpdate />} />
    <Route path=":id">
      <Route index element={<StudentLessonProgressDetail />} />
      <Route path="edit" element={<StudentLessonProgressUpdate />} />
      <Route path="delete" element={<StudentLessonProgressDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentLessonProgressRoutes;
