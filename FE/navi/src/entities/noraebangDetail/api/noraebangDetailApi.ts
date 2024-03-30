import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

export const noraebangDetailApi = async (
  props: number,
): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/noraebangs/detail/${props}`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get noraebang detail', error);
    return null;
  }
};
