import axios from 'axios';

export interface IStudentAchievement {
  id: number;
  code: string;
  name: string;
  description: string;
  iconUrl?: string;
  unlocked: boolean;
  unlockedAt?: string;
  current: number;
  target: number;
}

const apiUrl = 'api/student/achievements';

export const getStudentAchievements = async (): Promise<IStudentAchievement[]> => {
  const response = await axios.get<IStudentAchievement[]>(apiUrl);
  return response.data;
};
