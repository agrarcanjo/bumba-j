import { ILesson } from 'app/shared/model/lesson.model';
import { IQuestion } from 'app/shared/model/question.model';

export interface ILessonQuestion {
  id?: number;
  orderIndex?: number;
  lesson?: ILesson;
  question?: IQuestion;
}

export const defaultValue: Readonly<ILessonQuestion> = {};
