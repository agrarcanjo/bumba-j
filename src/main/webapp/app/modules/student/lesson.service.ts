import axios from 'axios';

export interface INextLesson {
  id: number;
  title: string;
  description: string;
}

export interface IQuestion {
  id: number;
  type: 'MULTIPLE_CHOICE' | 'FILL_BLANK' | 'LISTENING' | 'READING' | 'WRITING' | 'SPEAKING';
  questionText: string;
  options?: string[];
  audioUrl?: string;
  imageUrl?: string;
}

export interface ILessonStart {
  lessonId: number;
  title: string;
  description: string;
  questions: IQuestion[];
  totalQuestions: number;
}

export interface IAnswerResponse {
  correct: boolean;
  correctAnswer?: string;
  explanation?: string;
}

export interface ILessonComplete {
  score: number;
  xpGained: number;
  achievements: IAchievementUnlocked[];
  totalQuestions: number;
  correctAnswers: number;
}

export interface IAchievementUnlocked {
  id: number;
  code: string;
  name: string;
  description: string;
  iconUrl?: string;
}

const apiUrl = 'api/student/lessons';

export const getNextLesson = async (): Promise<INextLesson> => {
  const response = await axios.get<INextLesson>(`${apiUrl}/next`);
  return response.data;
};

export const startLesson = async (lessonId: number): Promise<ILessonStart> => {
  const response = await axios.get<ILessonStart>(`${apiUrl}/${lessonId}/start`);
  return response.data;
};

export const submitAnswer = async (lessonId: number, questionId: number, answer: string): Promise<IAnswerResponse> => {
  const response = await axios.post<IAnswerResponse>(`${apiUrl}/${lessonId}/questions/${questionId}/answer`, { answer });
  return response.data;
};

export const completeLesson = async (lessonId: number): Promise<ILessonComplete> => {
  const response = await axios.post<ILessonComplete>(`${apiUrl}/${lessonId}/complete`);
  return response.data;
};
