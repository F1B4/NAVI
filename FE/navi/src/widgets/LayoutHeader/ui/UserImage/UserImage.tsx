import { Link } from 'react-router-dom';
import css from './UserImage.module.css';

export function UserImage() {
  return (
    <Link to={'/cover'}>
      <div className={css.root}></div>
    </Link>
  );
}
