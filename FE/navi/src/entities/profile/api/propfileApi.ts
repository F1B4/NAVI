import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const profileApi = async (props: number): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/profile/${props}`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get profile', error);
    return null;
  }
};
