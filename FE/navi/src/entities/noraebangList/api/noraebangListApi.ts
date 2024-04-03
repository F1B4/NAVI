import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const noraebangListApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/noraebangs`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get noraebang list', error);
    return null;
  }
};

export const noraebangListByLikeApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/noraebangs/byLike`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get noraebang list', error);
    return null;
  }
};

export const noraebangListByViewApi = async (): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/noraebangs/byView`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get noraebang list', error);
    return null;
  }
};
