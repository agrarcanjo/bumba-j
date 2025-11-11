import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ClassRoom from './class-room';
import ClassRoomDetail from './class-room-detail';
import ClassRoomUpdate from './class-room-update';
import ClassRoomDeleteDialog from './class-room-delete-dialog';

const ClassRoomRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ClassRoom />} />
    <Route path="new" element={<ClassRoomUpdate />} />
    <Route path=":id">
      <Route index element={<ClassRoomDetail />} />
      <Route path="edit" element={<ClassRoomUpdate />} />
      <Route path="delete" element={<ClassRoomDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClassRoomRoutes;
