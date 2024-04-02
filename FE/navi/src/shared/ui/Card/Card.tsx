import { usePlayStore } from '@/shared/store/playStore/playStore';
interface CardProps {
  id: number;
  type: string;
  record?: string;
  video?: string;
  classCard: string;
  classImg: string;
  classDesc: string;
  thumbnail?: string;
  user?: string;
  info: {
    id: number;
    title: string;
    image: string;
    mr?: string;
    users?: string;
  };
}

export function Card(props: CardProps) {
  const store = usePlayStore();

  const handleClick = () => {
    store.goPlay({
      type: props.thumbnail ? 'cover' : 'noraebang',
      url: props.video ? props.video : props.record!,
      title: props.info.title,
      coverImage: props.thumbnail ? props.thumbnail : props.info.image,
      artist: props.thumbnail ? props.user : props.user!,
    });
  };

  const imageSrc = props.thumbnail ? props.thumbnail : props.info.image;
  return (
    <div className={props.classCard}>
      <div className={props.classImg} onClick={handleClick}>
        <img src={imageSrc} alt="image" />
      </div>
      <div className={props.classDesc} onClick={handleClick}>
        <strong>{props.info.title}</strong>
        {props.user}
      </div>
    </div>
  );
}
