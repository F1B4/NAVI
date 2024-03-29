import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const hotNoraebangListApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/main/noraebangs/hot`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get hot noraebang list', error);
    return null;
  }
};
