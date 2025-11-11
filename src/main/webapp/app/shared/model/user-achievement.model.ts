import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IAchievement } from 'app/shared/model/achievement.model';

export interface IUserAchievement {
  id?: number;
  unlockedAt?: dayjs.Dayjs;
  user?: IUser;
  achievement?: IAchievement;
}

export const defaultValue: Readonly<IUserAchievement> = {};
