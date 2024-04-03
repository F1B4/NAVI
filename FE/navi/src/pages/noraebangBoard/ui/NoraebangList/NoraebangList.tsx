import { useEffect, useState } from 'react';
import {
  noraebangListApi,
  noraebangListByLikeApi,
  noraebangListByViewApi,
} from '@/entities/noraebangList';
import type { NoraebangList } from '@/entities/noraebangList';
import { Card } from '@/shared/ui';
import css from './NoraebangList.module.css';

export function NoraebangList() {
  const [noraebangs, setNoraebangs] = useState<NoraebangList>();
  const [selectedOption, setSelectedOption] = useState('list');

  useEffect(() => {
    const AxiosNoraebangs = async () => {
      let response;
      try {
        switch (selectedOption) {
          case 'list':
            response = await noraebangListApi();
            break;
          case 'listByLike':
            response = await noraebangListByLikeApi();
            break;
          case 'listByView':
            response = await noraebangListByViewApi();
            break;
          default:
            response = await noraebangListApi();
        }

        if (response?.resultCode === 'OK') {
          setNoraebangs(response.data);
        }
      } catch (error) {
        console.error('Error get noraebangs list');
      }
    };
    AxiosNoraebangs();
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
      {Array.isArray(noraebangs) ? (
        noraebangs.map((noraebang, index) => (
          <Card
            record={noraebang.record}
            id={noraebang.id}
            key={index}
            classCard={css.card}
            classImg={css.img}
            classDesc={css.desc}
            user={noraebang.userDto.nickname}
            type="noraebang"
            info={noraebang.songDto}
          />
        ))
      ) : (
        <div className={css.center}>
          <span className="loading loading-spinner loading-lg"></span>
        </div>
      )}
    </div>
  );
}
