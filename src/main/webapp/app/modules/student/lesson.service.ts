import axios from 'axios';
import { IUser } from 'app/shared/model/user.model';
import { IAchievement } from 'app/shared/model/achievement.model';

export interface INextLesson {
  id: number;
  title: string;
  description: string;
}

export interface IQuestionContent {
  options?: string[];
  correctIndex?: number;
  sentence?: string;
  correctAnswer?: string;
  acceptedAnswers?: string[];
  audioUrl?: string;
  imageUrl?: string;
}

export interface IQuestion {
  id: number;
  type: 'MULTIPLE_CHOICE' | 'FILL_BLANK' | 'LISTENING' | 'READING' | 'WRITING' | 'SPEAKING';
  prompt: string;
  content: string;
  assets?: string;
  parsedContent?: IQuestionContent;
}

export interface ILessonDTO {
  lesson: {
    id: number;
    title: string;
    description: string;
  };
  questions: IQuestion[];
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
  newAchievements: IUserAchievement[];
}

export interface IAchievementUnlocked {
  id: number;
  unlockedAt: string;
  user: IUserAchievement;
  achievement: IAchievement;
}

export interface IUserAchievement {
  id: number;
  code: string;
  name: string;
  description: string;
  iconUrl?: string;
}

const apiUrl = 'api/student/lessons';

const parseQuestionContent = (question: IQuestion): IQuestion => {
  try {
    const parsedContent = JSON.parse(question.content) as IQuestionContent;
    return {
      ...question,
      parsedContent,
    };
  } catch (error) {
    console.error('Error parsing question content:', error);
    return question;
  }
};

export const getNextLesson = async (): Promise<INextLesson> => {
  const response = await axios.get<INextLesson>(`${apiUrl}/next`);
  return response.data;
};

export const startLesson = async (lessonId: number): Promise<ILessonStart> => {
  const response = await axios.get<ILessonDTO>(`${apiUrl}/${lessonId}/start`);
  const data = response.data;

  const questionsWithParsedContent = data.questions.map(parseQuestionContent);

  return {
    lessonId: data.lesson.id,
    title: data.lesson.title,
    description: data.lesson.description,
    questions: questionsWithParsedContent,
    totalQuestions: questionsWithParsedContent.length,
  };
};

export const submitAnswer = async (lessonId: number, questionId: number, answer: string): Promise<IAnswerResponse> => {
  const response = await axios.post<IAnswerResponse>(`${apiUrl}/${lessonId}/questions/${questionId}/answer`, { answer });
  return response.data;
};

export const completeLesson = async (lessonId: number): Promise<ILessonComplete> => {
  const response = await axios.post<ILessonComplete>(`${apiUrl}/${lessonId}/complete`);
  return response.data;
};
