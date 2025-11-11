export interface IAchievement {
  id?: number;
  code?: string;
  name?: string;
  iconUrl?: string | null;
  description?: string | null;
  criteria?: string;
}

export const defaultValue: Readonly<IAchievement> = {};
