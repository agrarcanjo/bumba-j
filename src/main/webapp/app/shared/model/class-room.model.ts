import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { Language } from 'app/shared/model/enumerations/language.model';

export interface IClassRoom {
  id?: number;
  name?: string;
  language?: keyof typeof Language;
  createdAt?: dayjs.Dayjs;
  description?: string | null;
  teacher?: IUser;
}

export const defaultValue: Readonly<IClassRoom> = {};
