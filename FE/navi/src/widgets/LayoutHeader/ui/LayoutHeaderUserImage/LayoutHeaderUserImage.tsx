import { Link } from 'react-router-dom';
import { UserImage } from '@/shared/ui';
import css from './LayoutHeaderUserImage.module.css';

export function LayoutHeaderUserImage() {
  return (
    <Link to={'/cover'}>
      <UserImage className={css.root} />
    </Link>
  );
}
