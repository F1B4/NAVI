/* eslint-disable react-hooks/rules-of-hooks */
import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';
import { usePlayStore } from '@/shared/store';

interface DetailProps {
  detailPk: number;
  userId: number;
}

export const noraebangDetailApi = async (
  props: DetailProps,
): Promise<Response | null> => {
  const play = usePlayStore();
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/noraebangs/detail/${play.pk}/${props.userId}`,
    );
    console.log(response);
    return response.data;
  } catch (error) {
    console.error('Error get noraebang detail', error);
    return null;
  }
};
