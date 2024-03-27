import type { NewContentsList } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const newContentsListApi = async (): Promise<NewContentsList | null> => {
  try {
    const response: AxiosResponse<NewContentsList> = await axios.get(
      `${baseApi}/main/new`,
    );
    return response.data;
  } catch (error) {
    console.error('Error  new Contents list', error);
    return null;
  }
};
