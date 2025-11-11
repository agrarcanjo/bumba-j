import dayjs from 'dayjs';
import { ITopic } from 'app/shared/model/topic.model';
import { IUser } from 'app/shared/model/user.model';
import { QuestionType } from 'app/shared/model/enumerations/question-type.model';
import { Language } from 'app/shared/model/enumerations/language.model';
import { Skill } from 'app/shared/model/enumerations/skill.model';
import { DifficultyLevel } from 'app/shared/model/enumerations/difficulty-level.model';

export interface IQuestion {
  id?: number;
  type?: keyof typeof QuestionType;
  language?: keyof typeof Language;
  skill?: keyof typeof Skill;
  level?: keyof typeof DifficultyLevel;
  createdAt?: dayjs.Dayjs;
  prompt?: string;
  content?: string;
  assets?: string | null;
  topic?: ITopic;
  createdBy?: IUser;
}

export const defaultValue: Readonly<IQuestion> = {};
