import { CoverList } from '../CoverList/CoverList';
import css from './Page.module.css';

export function CoverBoardPage() {
  return (
    <div className={css.root}>
      <h1>커버 게시판</h1>
      <CoverList />
    </div>
  );
}
