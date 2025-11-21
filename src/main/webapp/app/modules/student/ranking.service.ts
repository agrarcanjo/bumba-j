import axios from 'axios';

export interface IRankingEntry {
  position: number;
  userId: number;
  userName: string;
  totalXp: number;
  currentLevel: string;
  currentStreak: number;
  isCurrentUser: boolean;
}

export interface IRankingData {
  entries: IRankingEntry[];
  currentUserPosition: number;
  totalParticipants: number;
  lastUpdated: string;
}

export type RankingPeriod = 'week' | 'month' | 'all';

const apiUrl = 'api/student/ranking';

export const getRanking = async (period: RankingPeriod = 'all', municipalityCode?: string): Promise<IRankingData> => {
  const params: any = { period };
  if (municipalityCode) {
    params.municipality = municipalityCode;
  }
  const response = await axios.get<IRankingData>(apiUrl, { params });
  return response.data;
};
