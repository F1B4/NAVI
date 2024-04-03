import { useEffect, useState } from 'react';
import {
  coverListApi,
  coverListByLikeApi,
  coverListByViewApi,
} from '@/entities/coverList';
import type { CoverList, CoverUser } from '@/entities/coverList';
import { Card } from '@/shared/ui';
import css from './CoverList.module.css';

export function CoverList() {
  const [covers, setCovers] = useState<CoverList>();
  const [selectedOption, setSelectedOption] = useState('list');

  useEffect(() => {
    const AxiosCovers = async () => {
      let response;
      try {
        switch (selectedOption) {
          case 'list':
            response = await coverListApi();
            break;
          case 'listByLike':
            response = await coverListByLikeApi();
            break;
          case 'listByView':
            response = await coverListByViewApi();
            break;
          default:
            response = await coverListApi();
        }

        if (response?.resultCode === 'OK') {
          setCovers(response.data);
        }
      } catch (error) {
        console.error('Error get covers list');
      }
    };
    AxiosCovers();
  }, [selectedOption]);

  return (
    <div className={css.container}>
      <div className={css.dropdown}>
        <select
          value={selectedOption}
          onChange={(e) => setSelectedOption(e.target.value)}
        >
          <option value="list">최신순</option>
          <option value="listByLike">좋아요순</option>
          <option value="listByView">조회순</option>
        </select>
      </div>
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
