import type { SongInfo } from '@/shared/types';

interface CardProps {
  onClick?: (e: React.MouseEvent<HTMLDivElement>) => void;
  classCard: string;
  classImg: string;
  classDesc: string;
  info: SongInfo;
}

export function Card(props: CardProps) {
  return (
    <div className={props.classCard} onClick={props.onClick}>
      <div className={props.classImg}>
        <img src={props.info.image} alt="image" />
      </div>
      <div className={props.classDesc}>
        {props.info.title}
        {props.info.users}
      </div>
    </div>
  );
}
