import { baseApi } from '@/shared/api';
import type { userState } from '@/shared/store';
import axios from 'axios';

export const logOut = async (store: userState) => {
  try {
    console.log('try');
    const response = await axios.get(`${baseApi}/users/logout`, {
      withCredentials: true,
    });
    console.log('됨');
    console.log(response);
    if (response.data.resultCode === 'OK') {
      store.resetData();
    }
  } catch (error) {
    console.error('Error logout, error');
  }
};
