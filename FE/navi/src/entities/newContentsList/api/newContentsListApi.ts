import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const newContentsListApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/main/new`,
    );
    return response.data;
  } catch (error) {
    console.error('Error new Contents list', error);
    return null;
  }
};
