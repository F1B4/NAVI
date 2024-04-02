import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const FollowingApi = async (props: number): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/users/following/${props}`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get following', error);
    return null;
  }
};
