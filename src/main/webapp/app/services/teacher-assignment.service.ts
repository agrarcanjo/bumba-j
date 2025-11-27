import axios from 'axios';

export interface CreateAssignmentDTO {
  classRoomId: number;
  lessonId: number;
  dueDate: string;
}

export interface AssignmentDTO {
  id: number;
  classRoomId: number;
  classRoomName: string;
  lessonId: number;
  lessonTitle: string;
  assignedAt: string;
  dueDate: string;
  assignedByLogin: string;
}

const API_URL = '/api/teacher/assignments';

export const getAllAssignments = async (): Promise<AssignmentDTO[]> => {
  const response = await axios.get<AssignmentDTO[]>(API_URL);
  return response.data;
};

export const getAssignmentsByClassRoom = async (classRoomId: number): Promise<AssignmentDTO[]> => {
  const response = await axios.get<AssignmentDTO[]>(`${API_URL}/classroom/${classRoomId}`);
  return response.data;
};

export const createAssignment = async (assignment: CreateAssignmentDTO): Promise<AssignmentDTO> => {
  const response = await axios.post<AssignmentDTO>(API_URL, assignment);
  return response.data;
};

export const updateAssignment = async (id: number, assignment: CreateAssignmentDTO): Promise<AssignmentDTO> => {
  const response = await axios.put<AssignmentDTO>(`${API_URL}/${id}`, assignment);
  return response.data;
};

export const deleteAssignment = async (id: number): Promise<void> => {
  await axios.delete(`${API_URL}/${id}`);
};
