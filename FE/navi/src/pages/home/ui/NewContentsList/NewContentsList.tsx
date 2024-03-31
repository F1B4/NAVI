import { useEffect, useState } from 'react';
import { newContentsListApi } from '@/entities/newContentsList';
import type { NewContentsList, CoverUser } from '@/entities/newContentsList';
import { Card } from '@/shared/ui';
import css from './NewContentsList.module.css';

export function NewContentsList() {
  const [newContents, setNewContents] = useState<NewContentsList>();

  useEffect(() => {
    const AxiosNewContents = async () => {
      try {
        const response = await newContentsListApi();
        if (response?.resultCode === 'OK') {
          setNewContents(response.data);
        }
      } catch (error) {
        console.error('Error get new contents list');
      }
    };
    AxiosNewContents();
  }, []);

  return (
    <div className={css.container}>
      {Array.isArray(newContents) &&
        newContents.map((newContent, index) => {
          const type = newContent.thumbnail ? 'cover' : 'noraebang';

          const coverUserNicknames = newContent.coverUserDtos
            ? Array.from(
                new Set(
                  newContent.coverUserDtos.map(
                    (coverUserDto: CoverUser) => coverUserDto.userDto.nickname,
                  ),
                ),
              ).join(', ')
            : '';
          const user = newContent.thumbnail
            ? coverUserNicknames
            : newContent.userDto.nickname;
          return (
            <Card
              id={newContent.id}
              key={index}
              classCard={type === 'cover' ? css.coverCard : css.noraebangCard}
              classImg={type === 'cover' ? css.coverImg : css.noraebangImg}
              classDesc={type === 'cover' ? css.coverDesc : css.noraebangDesc}
              user={user}
              type={type}
              info={newContent.songDto}
            />
          );
        })}
    </div>
  );
}
