import { Logo } from '../Logo/Logo';
import { UserImage } from '../UserImage/UserImage';
import css from './LayoutHeader.module.css';

export function LayoutHeader() {
  return (
    <div className={css.root}>
      <Logo />
      <span>검색 바</span>
      <span>알림 아이콘</span>
      <UserImage />
    </div>
  );
}
