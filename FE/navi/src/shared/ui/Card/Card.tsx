import { baseUrl } from '@/shared/url';
import { Link } from 'react-router-dom';

interface CardProps {
  id: number;
  type: string;
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
  const imageSrc = props.thumbnail ? props.thumbnail : props.info.image;
  return (
    <div className={props.classCard}>
      <Link to={`${baseUrl}${props.type}/${props.id}`}>
        <div className={props.classImg}>
          <img src={imageSrc} alt="image" />
        </div>
      </Link>
      <div className={props.classDesc}>
        <Link to={`${baseUrl}${props.type}/${props.id}`}>
          <strong>{props.info.title}</strong>
        </Link>
        {props.user}
      </div>
    </div>
  );
}
