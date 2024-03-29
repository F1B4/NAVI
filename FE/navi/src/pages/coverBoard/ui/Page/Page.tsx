import { CoverList } from '../CoverList/CoverList';
import { Btn } from '@/shared/ui';
import css from './Page.module.css';

export function CoverBoardPage() {
  return (
    <div className={css.root}>
      <div className={css.title}>
        <h1>커버 게시판</h1>
        <Btn to={'/cover/post'} className={css.btn} content="글쓰기"></Btn>
      </div>
      <CoverList />
    </div>
  );
}
