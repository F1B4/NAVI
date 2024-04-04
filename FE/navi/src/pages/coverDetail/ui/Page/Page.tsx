import { useEffect, useState } from 'react';
import { coverDetailApi } from '@/entities/coverDetail';
import type { Cover } from '@/entities/coverDetail';
import { Info } from '../Info/Info';
import { Reviews } from '@/widgets/Reviews';
import { useUserStore } from '@/shared/store';
import { usePlayStore } from '@/shared/store';
import css from './Page.module.css';

export function CoverDetailPage() {
  const store = useUserStore();
  const play = usePlayStore();
  const [cover, setCover] = useState<Cover>();
  const [load, setLoad] = useState<boolean>(false);
  const [select, setSelect] = useState<'info' | 'review'>('info'); // 'info' 또는 'review'로 변경

  useEffect(() => {
    const AxiosCover = async () => {
      try {
        const response = await coverDetailApi({
          userId: store.userId,
          coverPk: play.pk,
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

  const getReview = () => {
    setSelect('review'); // 'review' 선택
  };

  const getInfo = () => {
    setSelect('info'); // 'info' 선택
  };

  if (load && cover) {
    return (
      <div className={css.root}>
        <div className={css.choice}>
          <div
            className={select === 'info' ? css.selected : ''}
            onClick={getInfo}
          >
            커버 정보
          </div>
          <div
            className={select === 'review' ? css.selected : ''}
            onClick={getReview}
          >
            리뷰 정보
          </div>
        </div>
        <div className={css.right}>
          {select === 'info' ? (
            <Info
              title={cover.title}
              image={cover.songDto.image}
              singers={cover.coverUserDtos}
            />
          ) : (
            <Reviews type="cover" data={cover.coverReviewDtos} pk={cover.id} />
          )}
        </div>
      </div>
    );
  }
  return null;
}
