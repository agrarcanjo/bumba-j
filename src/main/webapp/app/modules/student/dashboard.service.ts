import axios from 'axios';

export interface IAchievement {
  id: number;
  code: string;
  name: string;
  description: string;
  iconUrl?: string;
  unlockedAt: string;
}

export interface IDashboardData {
  totalXp: number;
  currentStreak: number;
  level: string;
  xpForNextLevel: number;
  progressToNextLevel: number;
  dailyGoal: number;
  dailyProgress: number;
  completedLessons: number;
  totalQuestions: number;
  correctAnswers: number;
  accuracyRate: number;
  recentAchievements: IAchievement[];
}

const apiUrl = 'api/student/dashboard';

export const getDashboard = async (): Promise<IDashboardData> => {
  const response = await axios.get<IDashboardData>(apiUrl);
  return response.data;
};
