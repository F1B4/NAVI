import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const coverListApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/covers`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get hot cover list', error);
    return null;
  }
};

export const coverListByLikeApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/covers/byLike`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get hot cover list', error);
    return null;
  }
};

export const coverListByViewApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/covers/byView`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get hot cover list', error);
    return null;
  }
};
