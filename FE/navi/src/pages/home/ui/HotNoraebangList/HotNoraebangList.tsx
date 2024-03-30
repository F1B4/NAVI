import { useEffect, useState } from 'react';
import { hotNoraebangListApi } from '@/entities/hotNoraebangList';
import type { NoraebangList } from '@/entities/hotNoraebangList';
import { Card } from '@/shared/ui';
import css from './HotNoraebangList.module.css';

export function HotNoraebangList() {
  const [noraebangs, setNoraebangs] = useState<NoraebangList>();

  useEffect(() => {
    const AxiosHotNoraebangs = async () => {
      try {
        const response = await hotNoraebangListApi();
        if (response?.resultCode === 'OK') {
          setNoraebangs(response.data);
        }
      } catch (error) {
        console.error('Error get hot noraebangs list');
      }
    };
    AxiosHotNoraebangs();
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
