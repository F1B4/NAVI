import type { CoverInfo } from '@/entities/coverDetail/api/types';
import css from './Info.module.css';

interface InfoProps {
  title: string;
  image: string;
  singers: CoverInfo[];
}

export function Info(props: InfoProps) {
  return (
    <div className={css.root}>
      <h1>커버 정보</h1>
      <div className={css.songInfo}>
        <div className={css.img}>
          <img src={props.image} alt="" />
        </div>
        <div className={css.title}>{props.title}</div>
      </div>
      <div className={css.userInfo}>
        {Array.isArray(props.singers) &&
          props.singers.map((singer, index) => (
            <div key={index}>
              <p>{singer.partDto.name}</p>
              <div className={css.img}>
                <img src={singer.partDto.image} alt="" />
              </div>
              <p>{singer.userDto.nickname}</p>
              <div className={css.img}>
                <img src={singer.userDto.image} alt="" />
              </div>
            </div>
          ))}
      </div>
    </div>
  );
}
