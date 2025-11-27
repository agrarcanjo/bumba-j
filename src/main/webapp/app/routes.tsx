import React from 'react';
import { Route } from 'react-router';

import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import EntitiesRoutes from 'app/entities/routes';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import { StudentDashboard, LessonPlayer, StudentRanking, StudentAchievements, StudentProfile, Lessons } from 'app/modules/student';
import { TeacherDashboard, TeacherClasses, TeacherClassDetail } from 'app/modules/teacher';

const loading = <div>loading ...</div>;

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => loading,
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => loading,
});
const AppRoutes = () => {
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route index element={<Home />} />
        <Route path="login" element={<Login />} />
        <Route path="logout" element={<Logout />} />
        <Route path="account">
          <Route
            path="*"
            element={
              <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]}>
                <Account />
              </PrivateRoute>
            }
          />
          <Route path="register" element={<Register />} />
          <Route path="activate" element={<Activate />} />
          <Route path="reset">
            <Route path="request" element={<PasswordResetInit />} />
            <Route path="finish" element={<PasswordResetFinish />} />
          </Route>
        </Route>
        <Route
          path="admin/*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
              <Admin />
            </PrivateRoute>
          }
        />
        <Route
          path="student/dashboard"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_STUDENT]}>
              <StudentDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="student/lessons"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_STUDENT]}>
              <Lessons />
            </PrivateRoute>
          }
        />
        <Route
          path="student/lesson/:id"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_STUDENT]}>
              <LessonPlayer />
            </PrivateRoute>
          }
        />
        <Route
          path="student/ranking"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_STUDENT, AUTHORITIES.USER]}>
              <StudentRanking />
            </PrivateRoute>
          }
        />
        <Route
          path="student/achievements"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_STUDENT]}>
              <StudentAchievements />
            </PrivateRoute>
          }
        />
        <Route
          path="student/profile"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_STUDENT]}>
              <StudentProfile />
            </PrivateRoute>
          }
        />
        <Route
          path="teacher/dashboard"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_TEACHER]}>
              <TeacherDashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="teacher/classes"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_TEACHER]}>
              <TeacherClasses />
            </PrivateRoute>
          }
        />
        <Route
          path="teacher/classes/:id"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ROLE_TEACHER]}>
              <TeacherClassDetail />
            </PrivateRoute>
          }
        />
        <Route
          path="*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.USER]}>
              <EntitiesRoutes />
            </PrivateRoute>
          }
        />
        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
