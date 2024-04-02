import { useEffect, useState } from 'react';
import { coverListApi } from '@/entities/coverList';
import type { CoverList, CoverUser } from '@/entities/coverList';
import { Card } from '@/shared/ui';
import css from './CoverList.module.css';

export function CoverList() {
  const [covers, setCovers] = useState<CoverList>();

  useEffect(() => {
    const AxiosCovers = async () => {
      try {
        const response = await coverListApi();
        if (response?.resultCode === 'OK') {
          setCovers(response.data);
        }
      } catch (error) {
        console.error('Error get covers list');
      }
    };
    AxiosCovers();
  }, []);

  return (
    <div className={css.container}>
      {Array.isArray(covers) ? (
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
              video={cover.video}
              id={cover.id}
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
        })
      ) : (
        <div className={css.center}>
          <span className="loading loading-spinner loading-lg"></span>
        </div>
      )}
    </div>
  );
}
