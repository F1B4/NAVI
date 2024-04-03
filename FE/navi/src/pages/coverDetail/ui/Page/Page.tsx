import { useEffect, useState } from 'react';
import { coverDetailApi } from '@/entities/coverDetail';
import type { Cover } from '@/entities/coverDetail';
import { Info } from '../Info/Info';
import { Reviews } from '@/widgets/Reviews';
import { useUserStore } from '@/shared/store';
import css from './Page.module.css';

export function CoverDetailPage(pk: number) {
  const store = useUserStore();
  const props = Number(pk);
  const [cover, setCover] = useState<Cover>();
  const [load, setLoad] = useState<boolean>(false);
  useEffect(() => {
    const AxiosCover = async () => {
      try {
        const response = await coverDetailApi({
          detailPk: props,
          userId: store.userId,
        });
        if (response?.resultCode === 'OK') {
          setCover(response.data);
          setLoad(true);
        }
      } catch (error) {
        console.error('Error get cover detail');
      }
    };
    AxiosCover();
  }, []);

  if (load && cover) {
    return (
      <div className={css.root}>
        <div className={css.right}>
          {/* 가수 정보 안들어옴 */}
          <Info title={cover.title} />
          <Reviews type="cover" data={cover.coverReviewDtos} />
        </div>
      </div>
    );
  }
  return null;
}
