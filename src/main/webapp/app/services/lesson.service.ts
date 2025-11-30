import axios from 'axios';

export interface LessonDTO {
  id: number;
  title: string;
  description: string;
  topicId: number;
  topicName?: string;
  difficulty: string;
  xpReward: number;
}

const API_URL = '/api/lessons';

export const getAllLessons = async (): Promise<LessonDTO[]> => {
  const response = await axios.get<LessonDTO[]>(API_URL);
  return response.data;
};

export const getLessonById = async (id: number): Promise<LessonDTO> => {
  const response = await axios.get<LessonDTO>(`${API_URL}/${id}`);
  return response.data;
};
