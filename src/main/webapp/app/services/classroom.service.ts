import axios from 'axios';

export interface CreateClassRoomDTO {
  name: string;
  language: string;
  description?: string;
}

export interface ClassRoomDTO {
  id: number;
  name: string;
  language: string;
  description?: string;
  createdAt: string;
  teacher: {
    id: number;
    login: string;
  };
}

const API_URL = '/api/teacher/class-rooms';

export const createClassRoom = async (classRoom: CreateClassRoomDTO): Promise<ClassRoomDTO> => {
  const response = await axios.post<ClassRoomDTO>(API_URL, classRoom);
  return response.data;
};

export const updateClassRoom = async (id: number, classRoom: CreateClassRoomDTO): Promise<ClassRoomDTO> => {
  const response = await axios.put<ClassRoomDTO>(`${API_URL}/${id}`, classRoom);
  return response.data;
};

export const deleteClassRoom = async (id: number): Promise<void> => {
  await axios.delete(`${API_URL}/${id}`);
};

export const getClassRoom = async (id: number): Promise<ClassRoomDTO> => {
  const response = await axios.get<ClassRoomDTO>(`${API_URL}/${id}`);
  return response.data;
};
