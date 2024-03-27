import { useEffect, useState } from 'react';
import { hotNoraebangListApi } from '@/entities/hotNoraebangList';
import type { NoraebangList } from '@/entities/hotNoraebangList/api/types';
import { Card } from '@/shared/ui';
import css from './HotNoraebangList.module.css';

export function HotNoraebangList() {
  const [noraebangs, setNoraebangs] = useState<NoraebangList>();

  useEffect(() => {
    const AxiosHotNoraebangs = async () => {
      try {
        const response = await hotNoraebangListApi();
        if (response !== null) {
          setNoraebangs(response);
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
            key={index}
            classCard={css.card}
            classImg={css.img}
            classDesc={css.desc}
            type="noraebang"
            info={noraebang.songDto}
          />
        ))}
    </div>
  );
}
