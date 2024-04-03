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
  const [select, setSelect] = useState<boolean>(true);
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
          console.log(response.data);
        }
      } catch (error) {
        console.error('Error get cover detail');
      }
    };
    AxiosCover();
  }, []);

  const getReview = () => {
    setSelect(false);
  };

  const getInfo = () => {
    setSelect(true);
  };

  if (load && cover) {
    console.log(cover);
    return (
      <div className={css.root}>
        <div className={css.choice}>
          <div onClick={getInfo}>커버 정보</div>
          <div onClick={getReview}>리뷰 정보</div>
        </div>
        <div className={css.right}>
          {select ? (
            <Info
              title={cover.title}
              image={cover.songDto.image}
              singers={cover.coverUserDtos}
            />
          ) : (
            <Reviews type="cover" data={cover.coverReviewDtos} />
          )}
        </div>
      </div>
    );
  }
  return null;
}
