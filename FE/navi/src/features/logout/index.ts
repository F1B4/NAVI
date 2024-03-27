import { baseApi } from '@/shared/api';
import axios from 'axios';

export const logOut = async () => {
  console.log('로그아웃시도');
  try {
    const response = await axios.get(`${baseApi}/users/logout`, {
      withCredentials: true,
    });
    console.log(response);
    console.log('로그아웃');
  } catch (error) {
    console.error('Error logout, error');
  }
};
