import { Link } from 'react-router-dom';
import { useUserStore } from '@/shared/store';
import css from './Alarm.module.css';

export function Alarm() {
  const store = useUserStore();
  if (store.isLogin) {
    return (
      <Link to={'/noraebang'}>
        <div className={css.root}></div>
      </Link>
    );
  }
  return null;
}
