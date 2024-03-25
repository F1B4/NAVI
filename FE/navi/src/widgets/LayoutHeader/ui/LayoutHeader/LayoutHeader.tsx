import { Logo } from '../Logo/Logo';
import { Searchbar } from '../Searchbar/Searchbar';
import { Alarm } from '../Alarm/Alarm';
import { UserImage } from '../UserImage/UserImage';
import css from './LayoutHeader.module.css';

export function LayoutHeader() {
  return (
    <div className={css.root}>
      <div className={css.left}>
        <Logo />
        <Searchbar />
      </div>
      <div className={css.right}>
        <Alarm />
        <UserImage />
      </div>
    </div>
  );
}