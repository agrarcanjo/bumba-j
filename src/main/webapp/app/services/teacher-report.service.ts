import axios from 'axios';

export interface StudentProgressDTO {
  studentId: number;
  studentLogin: string;
  firstName: string;
  lastName: string;
  completedLessons: number;
  totalAssignments: number;
  averageScore: number;
  currentStreak: number;
}

export interface ClassReportDTO {
  classRoomId: number;
  classRoomName: string;
  description: string;
  totalStudents: number;
  totalAssignments: number;
  averageProgress: number;
  averageScore: number;
  studentProgress: StudentProgressDTO[];
}

export interface LessonProgressDTO {
  lessonId: number;
  lessonTitle: string;
  completedAt: string;
  score: number;
  xpGained: number;
  attempts: number;
}

export interface StudentReportDTO {
  studentId: number;
  studentLogin: string;
  firstName: string;
  lastName: string;
  email: string;
  totalXp: number;
  level: string;
  currentStreak: number;
  completedLessons: number;
  averageScore: number;
  lessonProgress: LessonProgressDTO[];
}

const API_URL = '/api/teacher/reports';

export const getClassReport = async (classRoomId: number): Promise<ClassReportDTO> => {
  const response = await axios.get<ClassReportDTO>(`${API_URL}/class/${classRoomId}`);
  return response.data;
};

export const getStudentReport = async (studentId: number, classRoomId?: number): Promise<StudentReportDTO> => {
  const params = classRoomId ? { classRoomId } : {};
  const response = await axios.get<StudentReportDTO>(`${API_URL}/student/${studentId}`, { params });
  return response.data;
};
