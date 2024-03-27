import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface userState {
  userId: number;
}

const useUserStore = create(
  persist<userState>(
    () => ({
      userId: 0,
    }),
    {
      name: 'userStorage',
    },
  ),
);

export { useUserStore };
