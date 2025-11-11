import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserProfile from './user-profile';
import Topic from './topic';
import Question from './question';
import Lesson from './lesson';
import LessonQuestion from './lesson-question';
import ClassRoom from './class-room';
import ClassMember from './class-member';
import LessonAssignment from './lesson-assignment';
import StudentLessonProgress from './student-lesson-progress';
import Attempt from './attempt';
import Achievement from './achievement';
import UserAchievement from './user-achievement';
import Recommendation from './recommendation';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="topic/*" element={<Topic />} />
        <Route path="question/*" element={<Question />} />
        <Route path="lesson/*" element={<Lesson />} />
        <Route path="lesson-question/*" element={<LessonQuestion />} />
        <Route path="class-room/*" element={<ClassRoom />} />
        <Route path="class-member/*" element={<ClassMember />} />
        <Route path="lesson-assignment/*" element={<LessonAssignment />} />
        <Route path="student-lesson-progress/*" element={<StudentLessonProgress />} />
        <Route path="attempt/*" element={<Attempt />} />
        <Route path="achievement/*" element={<Achievement />} />
        <Route path="user-achievement/*" element={<UserAchievement />} />
        <Route path="recommendation/*" element={<Recommendation />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
