import { NoraebangList } from '../NoraebangList/NoraebangList';
import css from './Page.module.css';

export function NoraebangBoardPage() {
  return (
    <div className={css.root}>
      <h1>노래방 목록</h1>
      <NoraebangList />
    </div>
  );
}
