import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { baseApi } from '@/shared/api';
import axios from 'axios';

interface UserState {
  userId: number;
  nickname: string;
  image: string;
  followingCount: number;
  followerCount: number;
  role: string;
  isLogin: boolean;
  recordCount: number;
  getData: () => void;
  getRec: () => void;
  resetData: () => void;
}

const useUserStore = create(
  persist<UserState>(
    (set) => ({
      userId: 0,
      nickname: '',
      image: '',
      followingCount: 0,
      followerCount: 0,
      role: '',
      isLogin: false,
      recordCount: 0,
      getData: async () => {
        try {
          const response = await axios.get(`${baseApi}/users/info`, {
            withCredentials: true,
          });
          const responseData = response.data.data;
          set({
            userId: responseData.id,
            nickname: responseData.nickname,
            image: responseData.image,
            followingCount: responseData.followingCount,
            followerCount: responseData.followerCount,
            role: responseData.role,
            isLogin: true,
          });
        } catch (error) {
          console.error('Error get user info');
        }
      },
      getRec: async () => {
        try {
          const response = await axios.get(`${baseApi}/users/voice/count`, {
            withCredentials: true,
          });
          const Data = response.data.data;
          set({ recordCount: Data });
        } catch (error) {
          console.error('Error get user recordCount');
        }
      },
      resetData: () => {
        set({
          userId: 0,
          nickname: '',
          image: '',
          followingCount: 0,
          followerCount: 0,
          role: '',
          isLogin: false,
        });
      },
    }),
    {
      name: 'userStorage',
    },
  ),
);

export { useUserStore };
export type { UserState };
