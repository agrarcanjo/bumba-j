import dayjs from 'dayjs';
import { IClassRoom } from 'app/shared/model/class-room.model';
import { IUser } from 'app/shared/model/user.model';

export interface IClassMember {
  id?: number;
  enrolledAt?: dayjs.Dayjs;
  classRoom?: IClassRoom;
  student?: IUser;
}

export const defaultValue: Readonly<IClassMember> = {};
