import type { Response, NoraebangList } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const hotNoraebangListApi = async (): Promise<NoraebangList | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/main/noraebangs/hot`,
    );
    return response.data.data;
  } catch (error) {
    console.error('Error get hot noraebang list', error);
    return null;
  }
};
