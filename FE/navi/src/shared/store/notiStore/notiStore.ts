import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { baseApi } from '@/shared/api';
import axios from 'axios';

interface noti {
  id: number;
  content: string;
  alarmStatus: string;
}

interface NotiState {
  data: noti[];
  getAlarms: () => void;
}

interface PlayProps {
  type: string;
  url: string;
  title: string;
  coverImage: string;
  artist?: string;
}

const useNotiStore = create(
  persist<NotiState>(
    (set) => ({
      data: [],
      getAlarms: async () => {
        const response = await axios.get(`${baseApi}/alarms`, {
          withCredentials: true,
        });
        const Data = response.data.data;
        set({ data: Data });
      },
    }),
    {
      name: 'notiStrage',
    },
  ),
);

export { useNotiStore };
export type { NotiState, PlayProps };
