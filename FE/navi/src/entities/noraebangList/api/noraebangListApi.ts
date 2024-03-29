import type { Response, NoraebangList } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const noraebangListApi = async (): Promise<NoraebangList | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/noraebangs`,
      {
        withCredentials: true,
      },
    );
    return response.data.data;
  } catch (error) {
    console.error('Error get noraebang list', error);
    return null;
  }
};
