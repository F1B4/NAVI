import type { Response } from './types';
import { baseApi } from '@/shared/api';
import axios, { AxiosResponse } from 'axios';

interface DetailProps {
  detailPk: number;
  userId: number;
}

export const noraebangDetailApi = async (
  props: DetailProps,
): Promise<Response | null> => {
  try {
    const response: AxiosResponse<Response> = await axios.get(
      `${baseApi}/noraebangs/detail/${props.detailPk}/${props.userId}`,
    );
    console.log(props.detailPk, props.userId);
    console.log(baseApi);
    return response.data;
  } catch (error) {
    console.log(props.detailPk, props.userId);
    console.log(baseApi);
    console.error('Error get noraebang detail', error);
    return null;
  }
};
