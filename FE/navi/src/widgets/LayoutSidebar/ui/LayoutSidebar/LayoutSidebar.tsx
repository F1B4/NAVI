import { Btn } from '@/shared/ui';
import css from './LayoutSidebar.module.css';

export function LayoutSidebar() {
  return (
    <div className={css.root}>
      <Btn
        to="/"
        className={css.btn}
        icon="images/home_icon.png"
        content="홈"
      ></Btn>
      <Btn
        to="/cover"
        className={css.btn}
        icon="images/cover_icon.png"
        content="커버"
      ></Btn>
      <Btn
        to="/noraebang"
        className={css.btn}
        icon="images/noraebang_icon.png"
        content="노래방"
      ></Btn>
      <hr />
    </div>
  );
}
