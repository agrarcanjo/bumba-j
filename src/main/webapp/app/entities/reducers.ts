import userProfile from 'app/entities/user-profile/user-profile.reducer';
import topic from 'app/entities/topic/topic.reducer';
import question from 'app/entities/question/question.reducer';
import lesson from 'app/entities/lesson/lesson.reducer';
import lessonQuestion from 'app/entities/lesson-question/lesson-question.reducer';
import classRoom from 'app/entities/class-room/class-room.reducer';
import classMember from 'app/entities/class-member/class-member.reducer';
import lessonAssignment from 'app/entities/lesson-assignment/lesson-assignment.reducer';
import studentLessonProgress from 'app/entities/student-lesson-progress/student-lesson-progress.reducer';
import attempt from 'app/entities/attempt/attempt.reducer';
import achievement from 'app/entities/achievement/achievement.reducer';
import userAchievement from 'app/entities/user-achievement/user-achievement.reducer';
import recommendation from 'app/entities/recommendation/recommendation.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  topic,
  question,
  lesson,
  lessonQuestion,
  classRoom,
  classMember,
  lessonAssignment,
  studentLessonProgress,
  attempt,
  achievement,
  userAchievement,
  recommendation,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
