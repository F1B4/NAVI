import { baseApi } from '@/shared/api';
import type { UserState } from '@/shared/store';
import axios from 'axios';

export const logOut = async (store: UserState) => {
  try {
    const response = await axios.get(`${baseApi}/users/logout`, {
      withCredentials: true,
    });
    if (response.data.resultCode === 'OK') {
      store.resetData();
    }
  } catch (error) {
    console.error('Error logout, error');
  }
};
