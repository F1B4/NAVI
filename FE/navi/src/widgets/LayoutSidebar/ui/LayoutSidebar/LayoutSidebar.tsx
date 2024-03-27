import { Link } from 'react-router-dom';
import css from './LayoutSidebar.module.css';

export function LayoutSidebar() {
  return (
    <div className={css.root}>
      <Link to={'/'}>홈</Link>
      <Link to={'/cover'}>커버</Link>
      <Link to={'/noraebang'}>노래방</Link>
    </div>
  );
}
