import { Link } from 'react-router-dom';
import { UserImage } from '@/shared/ui';
import { useUserStore } from '@/shared/store';
import css from './LayoutHeaderUserImage.module.css';

export function LayoutHeaderUserImage() {
  const store = useUserStore();
  if (store.isLogin) {
    return (
      <Link to={`/profile/${store.userId}`}>
        <UserImage image={store.image} className={css.root} />
      </Link>
    );
  }
  return null;
}
