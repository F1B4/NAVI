import { Card } from '@/shared/ui';
import type {
  NoraebangLikeDto,
  CoverUserDto,
  CoverLikeDto,
} from '@/entities/profile/api/profile/types';
import css from './LikeList.module.css';

interface LikeProps {
  likeNorae: NoraebangLikeDto[];
  likeCover: CoverLikeDto[];
}

export function LikeList(props: LikeProps) {
  return (
    <div>
      {props.likeNorae.length > 0 && (
        <div className={css.container}>
          {Array.isArray(props.likeNorae) &&
            props.likeNorae.map((likeNorae, index) => {
              return (
                <Card
                  id={likeNorae.noraebangDto.id}
                  key={index}
                  classCard={css.noraebangCard}
                  classImg={css.noraebangImg}
                  classDesc={css.noraebangDesc}
                  user={likeNorae.noraebangDto.userDto.nickname}
                  type={'noraebang'}
                  info={likeNorae.noraebangDto.songDto}
                />
              );
            })}
        </div>
      )}

      {props.likeCover.length > 0 && (
        <div className={css.container}>
          {Array.isArray(props.likeCover) &&
            props.likeCover.map((likeCover, index) => {
              const Nicknames = likeCover.coverDto.coverUserDtos
                ? Array.from(
                    new Set(
                      likeCover.coverDto.coverUserDtos.map(
                        (coverUserDto: CoverUserDto) =>
                          coverUserDto.userDto.nickname,
                      ),
                    ),
                  ).join(', ')
                : '';
              return (
                <Card
                  id={likeCover.coverDto.id}
                  key={index}
                  classCard={css.coverCard}
                  classImg={css.coverImg}
                  classDesc={css.coverDesc}
                  user={Nicknames}
                  type={'cover'}
                  info={likeCover.coverDto.songDto}
                />
              );
            })}
        </div>
      )}
      {props.likeCover.length === 0 && props.likeNorae.length === 0 && (
        <div className={css.container}>해당 노래가 없습니다</div>
      )}
    </div>
  );
}
