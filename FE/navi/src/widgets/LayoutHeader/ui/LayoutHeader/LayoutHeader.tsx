import { Logo } from '../Logo/Logo';
import { Searchbar } from '../Searchbar/Searchbar';
import { LogOutBtn } from '@/features/logout/';
import { Alarm } from '../Alarm/Alarm';
import SSEComponent from '@/features/notification/ui/Noti/Noti';
import { LayoutHeaderUserImage } from '../LayoutHeaderUserImage/LayoutHeaderUserImage';
import css from './LayoutHeader.module.css';
import { Login } from '../Login/Login';

export function LayoutHeader() {
  return (
    <div className={css.root}>
      <div className={css.left}>
        <Logo />
        <Searchbar />
      </div>
      <div className={css.right}>
        <SSEComponent />
        <LogOutBtn />
        <Alarm />
        <LayoutHeaderUserImage />
        <Login />
      </div>
    </div>
  );
}
