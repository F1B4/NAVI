import { useEffect, useState } from 'react';
import { hotCoverListApi } from '@/entities/hotCoverList';
import type { CoverList, CoverUser } from '@/entities/hotCoverList';
import { Card } from '@/shared/ui';
import css from './HotCoverList.module.css';

export function HotCoverList() {
  const [covers, setCovers] = useState<CoverList>();

  useEffect(() => {
    const AxiosHotCovers = async () => {
      try {
        const response = await hotCoverListApi();
        if (response !== null) {
          setCovers(response);
          console.log(response);
        }
      } catch (error) {
        console.error('Error get hot cover list');
      }
    };
    AxiosHotCovers();
  }, []);

  return (
    <div className={css.container}>
      {Array.isArray(covers) &&
        covers.map((cover, index) => {
          const coverUserNicknames = cover.coverUserDtos
            ? Array.from(
                new Set(
                  cover.coverUserDtos.map(
                    (coverUserDto: CoverUser) => coverUserDto.userDto.nickname,
                  ),
                ),
              ).join(', ')
            : '';

          return (
            <Card
              key={index}
              classCard={css.card}
              classImg={css.img}
              classDesc={css.desc}
              thumbnail={cover.thumbnail}
              user={coverUserNicknames}
              type="cover"
              info={cover.songDto}
            />
          );
        })}
    </div>
  );
}
