import dayjs from 'dayjs';
import { ITopic } from 'app/shared/model/topic.model';
import { IUser } from 'app/shared/model/user.model';
import { Language } from 'app/shared/model/enumerations/language.model';
import { DifficultyLevel } from 'app/shared/model/enumerations/difficulty-level.model';

export interface ILesson {
  id?: number;
  title?: string;
  language?: keyof typeof Language;
  level?: keyof typeof DifficultyLevel;
  xpReward?: number;
  passThreshold?: number;
  createdAt?: dayjs.Dayjs;
  description?: string | null;
  topic?: ITopic;
  createdBy?: IUser;
}

export const defaultValue: Readonly<ILesson> = {};
