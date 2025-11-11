import dayjs from 'dayjs';
import { IClassRoom } from 'app/shared/model/class-room.model';
import { ILesson } from 'app/shared/model/lesson.model';
import { IUser } from 'app/shared/model/user.model';

export interface ILessonAssignment {
  id?: number;
  assignedAt?: dayjs.Dayjs;
  dueDate?: dayjs.Dayjs;
  classRoom?: IClassRoom;
  lesson?: ILesson;
  assignedBy?: IUser;
}

export const defaultValue: Readonly<ILessonAssignment> = {};
