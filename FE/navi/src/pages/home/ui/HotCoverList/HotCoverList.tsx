import { useEffect, useState } from 'react';
import { hotCoverListApi } from '@/entities/hotCoverList';
import type { CoverList } from '@/entities/hotCoverList/api/types';
import { Card } from '@/shared/ui';
import css from './HotCoverList.module.css';

export function HotCoverList() {
  const [covers, setCovers] = useState<CoverList>([]);

  useEffect(() => {
    const AxiosHotCovers = async () => {
      try {
        const response = await hotCoverListApi();
        if (response !== null) {
          setCovers(response);
        }
      } catch (error) {
        console.error('Error get hot cover list');
      }
    };
    AxiosHotCovers();
  }, []);
  return (
    <div className={css.container}>
      {/* {covers.map((cover, index) => (
        <Card
          key={index}
          classCard={css.card}
          classImg={css.img}
          classDesc={css.desc}
          type="cover"
          info={cover.songDto}
        />
      ))} */}
    </div>
  );
}
