import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { coverDetailApi } from '@/entities/coverDetail';
import type { Cover } from '@/entities/coverDetail';
import { CoverDetail } from '../CoverDetail/CoverDetail';
// import { Info } from '../Info/Info';
// import { Reviews } from '@/widgets/Reviews';
import css from './Page.module.css';

export function CoverDetailPage() {
  const { coverPk } = useParams();
  const props = Number(coverPk);
  const [cover, setCover] = useState<Cover>();
  const [load, setLoad] = useState<boolean>(false);
  useEffect(() => {
    const AxiosCover = async () => {
      try {
        const response = await coverDetailApi(props);
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
        <div className={css.left}>
          <CoverDetail video={cover.video} />
          바보
        </div>
        <div className={css.right}>
          {/* <Info /> */}
          {/* <Reviews type="covers" data={cover.coverReviewDtos} /> */}
        </div>
      </div>
    );
  }
  return null;
}
