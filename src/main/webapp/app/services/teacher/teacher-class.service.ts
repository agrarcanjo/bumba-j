import axios from 'axios';

export interface StudentSummary {
  id: number;
  login: string;
  firstName: string;
  lastName: string;
  joinedAt: string;
  completedLessons: number;
}

export interface LessonAssignment {
  id: number;
  assignedAt: string;
  dueDate: string;
}

export interface ClassDetail {
  id: number;
  name: string;
  description: string;
  language: string;
  createdAt: string;
  students: StudentSummary[];
  assignments: LessonAssignment[];
}

export interface ClassRoom {
  id: number;
  name: string;
  description: string;
  language: string;
  createdAt: string;
}

export const getTeacherClasses = async (): Promise<ClassRoom[]> => {
  const response = await axios.get<ClassRoom[]>('/api/teacher/classes');
  return response.data;
};

export const getClassDetail = async (classId: number): Promise<ClassDetail> => {
  const response = await axios.get<ClassDetail>(`/api/teacher/classes/${classId}`);
  return response.data;
};
