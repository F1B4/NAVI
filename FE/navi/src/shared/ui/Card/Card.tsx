import { Link } from 'react-router-dom';

interface CardProps {
  type: string;
  classCard: string;
  classImg: string;
  classDesc: string;
  thumbnail?: string;
  info: {
    id: number;
    title: string;
    image: string;
    mr?: string;
    users?: string;
  };
}

export function Card(props: CardProps) {
  return (
    <Link to={`/${props.type}/${props.info.id}`}>
      <div className={props.classCard}>
        <div className={props.classImg}>
          <img src={props.info.image} alt="image" />
        </div>
        <div className={props.classDesc}>
          {props.info.title}
          {props.info.users}
        </div>
      </div>
    </Link>
  );
}
