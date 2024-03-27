import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { baseApi } from '@/shared/api';
import axios from 'axios';

interface userState {
  userId: number;
  nickname: string;
  image: string;
  followingCount: number;
  followerCount: number;
  role: string;
  isLogin: boolean;
  getData: () => void;
}

const useUserStore = create(
  persist<userState>(
    (set) => ({
      userId: 0,
      nickname: '',
      image: '',
      followingCount: 0,
      followerCount: 0,
      role: '',
      isLogin: false,
      getData: async () => {
        try {
          const response = await axios.get(`${baseApi}/users/info`, {
            withCredentials: true,
          });
          console.log(response);
          set({
            userId: response.data.userId,
            nickname: response.data.nickname,
            image: response.data.image,
            followingCount: response.data.followingCount,
            followerCount: response.data.followerCount,
            role: response.data.role,
            isLogin: true,
          });
        } catch (error) {
          console.error('Error get user info');
        }
      },
    }),
    {
      name: 'userStorage',
    },
  ),
);

export { useUserStore };
