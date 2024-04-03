import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';
import { usePlayStore } from '@/shared/store';

interface DetailProps {
  userId: number;
}

export const coverDetailApi = async (
  props: DetailProps,
): Promise<Response | null> => {
  const play = usePlayStore();
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/covers/detail/${play.pk}/${props.userId}`,
    );
    return response.data;
  } catch (error) {
    console.error('Error get cover detail', error);
    return null;
  }
};
