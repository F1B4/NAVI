import { NewContentsList } from '../NewContentsList/NewContentsList';
import { HotCoverList } from '../HotCoverList/HotCoverList';
import { HotNoraebangList } from '../HotNoraebangList/HotNoraebangList';

import css from './Page.module.css';

export function HomePage() {
  return (
    <>
      <div className={css.root}>
        <h1>최신 컨텐츠</h1>
        <NewContentsList />
        <h1>인기 커버</h1>
        <HotCoverList />
        <h1>인기 노래방</h1>
        <HotNoraebangList />
      </div>
    </>
  );
}
