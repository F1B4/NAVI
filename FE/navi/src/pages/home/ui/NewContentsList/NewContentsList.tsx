import { useEffect, useState } from 'react';
import { newContentsListApi } from '@/entities/newContentsList';
import type { NewContentsList } from '@/entities/newContentsList/api/types';
import { Card } from '@/shared/ui';
import css from './NewContentsList.module.css';

export function NewContentsList() {
  const [noraebangs, setNewContents] = useState<NewContentsList>([]);

  useEffect(() => {
    const AxiosNewContents = async () => {
      try {
        const response = await newContentsListApi();
        if (response !== null) {
          setNewContents(response);
        }
      } catch (error) {
        console.error('Error get new contents list');
      }
    };
    AxiosNewContents();
  }, []);
  return (
    <div className={css.container}>
      {noraebangs.map((noraebang, index) => (
        // 이쪽에서 type에 따라 처리해줘야 할 듯 계산하거나
        <Card
          key={index}
          classCard={css.card}
          classImg={css.img}
          classDesc={css.desc}
          type=""
          info={noraebang.songDto}
        />
      ))}
    </div>
  );
}
