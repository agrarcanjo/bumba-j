import axios from 'axios';

export interface IProfileStatistics {
  totalLessonsCompleted: number;
  totalQuestionsAnswered: number;
  totalCorrectAnswers: number;
  accuracyRate: number;
  totalAchievementsUnlocked: number;
  totalDaysActive: number;
  longestStreak: number;
}

export interface IRecentActivity {
  type: string;
  description: string;
  date: string;
  xpEarned: number;
}

export interface IStudentProfile {
  userId: number;
  login: string;
  firstName: string;
  lastName: string;
  email: string;
  municipalityCode: string;
  currentLevel: string;
  totalXp: number;
  currentStreak: number;
  dailyGoalXp: number;
  lastActivityDate: string;
  statistics: IProfileStatistics;
  recentActivities: IRecentActivity[];
}

export interface IUpdateDailyGoalRequest {
  dailyGoalXp: number;
}

const apiUrl = 'api/student/profile';

export const getProfile = async (): Promise<IStudentProfile> => {
  const response = await axios.get<IStudentProfile>(apiUrl);
  return response.data;
};

export const updateDailyGoal = async (dailyGoalXp: number): Promise<IStudentProfile> => {
  const response = await axios.put<IStudentProfile>(`${apiUrl}/daily-goal`, { dailyGoalXp });
  return response.data;
};
