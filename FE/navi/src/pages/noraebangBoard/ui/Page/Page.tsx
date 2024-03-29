import { NoraebangList } from '../NoraebangList/NoraebangList';
import { Btn } from '@/shared/ui';
import css from './Page.module.css';

export function NoraebangBoardPage() {
  return (
    <div className={css.root}>
      <div className={css.title}>
        <h1>노래방 목록</h1>
        <Btn to={'/noraebang/post'} className={css.btn} content="글쓰기"></Btn>
      </div>
      <NoraebangList />
    </div>
  );
}
