import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const coverDetailApi = async (
  props: number,
): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/covers/${props}`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get cover detail', error);
    return null;
  }
};
