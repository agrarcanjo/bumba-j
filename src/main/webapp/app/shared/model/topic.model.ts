import { Language } from 'app/shared/model/enumerations/language.model';

export interface ITopic {
  id?: number;
  name?: string;
  language?: keyof typeof Language;
  description?: string | null;
}

export const defaultValue: Readonly<ITopic> = {};
