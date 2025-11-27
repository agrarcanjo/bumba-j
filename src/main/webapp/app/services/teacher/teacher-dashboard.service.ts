import axios from 'axios';

export interface ClassOverview {
  id: number;
  name: string;
  description: string;
  studentCount: number;
  assignmentCount: number;
  completedLessons: number;
}

export interface RecentActivity {
  studentName: string;
  lessonTitle: string;
  completedAt: string;
  score: number;
}

export interface TeacherDashboard {
  totalClasses: number;
  totalStudents: number;
  totalAssignments: number;
  classes: ClassOverview[];
  recentActivities: RecentActivity[];
}

export const getTeacherDashboard = async (): Promise<TeacherDashboard> => {
  const response = await axios.get<TeacherDashboard>('/api/teacher/dashboard');
  return response.data;
};
