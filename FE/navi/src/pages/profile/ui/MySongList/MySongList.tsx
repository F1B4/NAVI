import { Card } from '@/shared/ui';
import type {
  CoverDto,
  NoraebangDto,
  CoverUserDto,
} from '@/entities/profile/api/profile/types';
import css from './MySongList.module.css';

interface MySongProps {
  myNorae: NoraebangDto[];
  myCover: CoverDto[];
}

export function MySongList(props: MySongProps) {
  console.log(props);
  return (
    <div>
      {props.myNorae.length > 0 && (
        <div className={css.container}>
          {Array.isArray(props.myNorae) &&
            props.myNorae.map((myNorae, index) => {
              return (
                <Card
                  id={myNorae.id}
                  key={index}
                  classCard={css.noraebangCard}
                  classImg={css.noraebangImg}
                  classDesc={css.noraebangDesc}
                  user={myNorae.userDto.nickname}
                  type={'noraebang'}
                  info={myNorae.songDto}
                />
              );
            })}
        </div>
      )}
      {props.myCover.length > 0 && (
        <div className={css.container}>
          {Array.isArray(props.myCover) &&
            props.myCover.map((myCover, index) => {
              const Nicknames = myCover.coverUserDtos
                ? Array.from(
                    new Set(
                      myCover.coverUserDtos.map(
                        (coverUserDto: CoverUserDto) =>
                          coverUserDto.userDto.nickname,
                      ),
                    ),
                  ).join(', ')
                : '';
              return (
                <Card
                  id={myCover.id}
                  key={index}
                  classCard={css.coverCard}
                  classImg={css.coverImg}
                  classDesc={css.coverDesc}
                  user={Nicknames}
                  type={'cover'}
                  info={myCover.songDto}
                />
              );
            })}
        </div>
      )}
      {props.myCover.length === 0 && props.myNorae.length === 0 && (
        <div className={css.container}>해당 노래가 없습니다</div>
      )}
    </div>
  );
}
