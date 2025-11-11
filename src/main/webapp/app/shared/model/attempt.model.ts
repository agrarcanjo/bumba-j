import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IQuestion } from 'app/shared/model/question.model';
import { ILesson } from 'app/shared/model/lesson.model';

export interface IAttempt {
  id?: number;
  isCorrect?: boolean;
  timeSpentSeconds?: number;
  attemptedAt?: dayjs.Dayjs;
  answer?: string;
  metadata?: string | null;
  student?: IUser;
  question?: IQuestion;
  lesson?: ILesson;
}

export const defaultValue: Readonly<IAttempt> = {
  isCorrect: false,
};
