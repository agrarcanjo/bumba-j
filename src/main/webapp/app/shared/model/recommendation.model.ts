import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ILesson } from 'app/shared/model/lesson.model';

export interface IRecommendation {
  id?: number;
  recommendedAt?: dayjs.Dayjs;
  wasCompleted?: boolean;
  reason?: string | null;
  student?: IUser;
  lesson?: ILesson;
}

export const defaultValue: Readonly<IRecommendation> = {
  wasCompleted: false,
};
