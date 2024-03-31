import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { noraebangDetailApi } from '@/entities/noraebangDetail';
import type { Noraebang } from '@/entities/noraebangDetail';
import { NoraebangDetail } from '../NoraebangDetail/NoraebangDetail';
import { Info } from '../Info/Info';
import { Reviews } from '@/widgets/Reviews';
import css from './Page.module.css';

export function NoraebangDetailPage() {
  const { noraebangPk } = useParams();
  const props = Number(noraebangPk);
  const [noraebang, setNoraebang] = useState<Noraebang>();
  const [load, setLoad] = useState<boolean>(false);
  useEffect(() => {
    const AxiosNoraebang = async () => {
      try {
        const response = await noraebangDetailApi(props);
        if (response?.resultCode === 'OK') {
          setNoraebang(response.data);
          setLoad(true);
        }
      } catch (error) {
        console.error('Error get norabang detail');
      }
    };
    AxiosNoraebang();
  }, []);

  if (load && noraebang) {
    return (
      <div className={css.root}>
        <div className={css.left}>
          <NoraebangDetail image={noraebang.songDto.image} />
        </div>
        <div className={css.right}>
          <Info
            title={noraebang.songDto.title}
            user={noraebang.userDto.nickname}
            content={noraebang.content}
          />
          <Reviews type="noraebangs" data={noraebang.noraebangReviewDtos} />
        </div>
      </div>
    );
  }
  return null;
}
