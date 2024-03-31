import { useEffect, useState } from 'react';
import { noraebangListApi } from '@/entities/noraebangList';
import type { NoraebangList } from '@/entities/noraebangList';
import { Card } from '@/shared/ui';
import css from './NoraebangList.module.css';

export function NoraebangList() {
  const [noraebangs, setNoraebangs] = useState<NoraebangList>();

  useEffect(() => {
    const AxiosNoraebangs = async () => {
      try {
        const response = await noraebangListApi();
        if (response?.resultCode === 'OK') {
          setNoraebangs(response.data);
        }
      } catch (error) {
        console.error('Error get noraebangs list');
      }
    };
    AxiosNoraebangs();
  }, []);

  return (
    <div className={css.container}>
      {Array.isArray(noraebangs) &&
        noraebangs.map((noraebang, index) => (
          <Card
            id={noraebang.id}
            key={index}
            classCard={css.card}
            classImg={css.img}
            classDesc={css.desc}
            user={noraebang.userDto.nickname}
            type="noraebang"
            info={noraebang.songDto}
          />
        ))}
    </div>
  );
}
