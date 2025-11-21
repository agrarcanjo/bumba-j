import axios from 'axios';

export interface IRankingEntry {
  userId: number;
  userName: string;
  totalXp: number;
  currentLevel: string;
  municipalityCode: string;
  rank: number;
  currentStreak: number;
  isCurrentUser: boolean;
}

export interface IRankingData {
  rankings: IRankingEntry[];
  currentUserRank: number;
  totalUsers: number;
  municipalityCode: string;
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
