import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

interface DetailProps {
  detailPk: number;
  userId: number;
}

export const coverDetailApi = async (
  props: DetailProps,
): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/covers/${props.detailPk}/${props.userId}`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get cover detail', error);
    return null;
  }
};
