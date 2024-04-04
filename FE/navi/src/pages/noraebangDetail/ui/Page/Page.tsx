import { useEffect, useState } from 'react';
import { noraebangDetailApi } from '@/entities/noraebangDetail';
import type { Noraebang } from '@/entities/noraebangDetail';
import { Info } from '../Info/Info';
import { Reviews } from '@/widgets/Reviews';
import { useUserStore } from '@/shared/store';
import css from './Page.module.css';

export function NoraebangDetailPage(pk: number) {
  const store = useUserStore();
  const props = Number(pk);
  const [noraebang, setNoraebang] = useState<Noraebang>();
  const [load, setLoad] = useState<boolean>(false);
  useEffect(() => {
    const AxiosNoraebang = async () => {
      try {
        const response = await noraebangDetailApi({
          detailPk: props,
          userId: store.userId,
        });
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
        <div className={css.right}>
          <Info
            title={noraebang.songDto.title}
            user={noraebang.userDto.nickname}
            content={noraebang.content}
          />
          <Reviews
            type="noraebang"
            data={noraebang.noraebangReviewDtos}
            pk={props}
          />
        </div>
      </div>
    );
  }
  return null;
}
