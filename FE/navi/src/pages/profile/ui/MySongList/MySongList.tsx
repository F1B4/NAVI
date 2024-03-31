// import { Card } from '@/shared/ui';
import css from './MySongList.module.css';

interface MySongProps {
  mySongList: number;
}

export function MySongList(props: MySongProps) {
  console.log(props)
  return (
    <div className={css.container}>
      {/* {Array.isArray(props.mySongList) &&
        props.mySongList.map((mySong, index) => {
            const 

            return (
                <Card
                  id={mySong.id}
                  key={index}
                  classCard={type === 'cover' ? css.coverCard : css.noraebangCard}
                  classImg={type === 'cover' ? css.coverImg : css.noraebangImg}
                  classDesc={type === 'cover' ? css.coverDesc : css.noraebangDesc}
                  user={user}
                  type={type}
                  info={mySong.songDto}
                />
              );
            })}; */}
    </div>
  );
}
