import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { UserLevel } from 'app/shared/model/enumerations/user-level.model';

export interface IUserProfile {
  id?: number;
  municipalityCode?: string;
  currentLevel?: keyof typeof UserLevel;
  totalXp?: number;
  currentStreak?: number;
  dailyGoalXp?: number;
  lastActivityDate?: dayjs.Dayjs | null;
  user?: IUser;
}

export const defaultValue: Readonly<IUserProfile> = {};
