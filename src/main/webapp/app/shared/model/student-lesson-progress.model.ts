import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ILesson } from 'app/shared/model/lesson.model';
import { ILessonAssignment } from 'app/shared/model/lesson-assignment.model';
import { LessonStatus } from 'app/shared/model/enumerations/lesson-status.model';

export interface IStudentLessonProgress {
  id?: number;
  status?: keyof typeof LessonStatus;
  score?: number | null;
  xpEarned?: number | null;
  completedAt?: dayjs.Dayjs | null;
  isLate?: boolean | null;
  student?: IUser;
  lesson?: ILesson;
  assignment?: ILessonAssignment | null;
}

export const defaultValue: Readonly<IStudentLessonProgress> = {
  isLate: false,
};
