import { useEffect } from 'react';
import { NewContentsList } from '../NewContentsList/NewContentsList';
import { HotCoverList } from '../HotCoverList/HotCoverList';
import { HotNoraebangList } from '../HotNoraebangList/HotNoraebangList';
import { useUserStore } from '@/shared/store';
import css from './Page.module.css';
// import { logOut } from '@/features/logout';

export function HomePage() {
  const store = useUserStore();
  const search = window.location.search;
  const params = new URLSearchParams(search);
  const loginSuccess = params.get('loginSuccess');
  useEffect(() => {
    const fetchData = async () => {
      if (loginSuccess && store.userId === 0) {
        store.getData();
      }
    };
    fetchData();
  });

  return (
    <div className={css.root}>
      {/* <div onClick={logOut}>로그아웃</div> */}
      <h1>최신 컨텐츠</h1>
      <NewContentsList />
      <h1>인기 커버</h1>
      <div className={css.cover}>
        <HotCoverList />
      </div>
      <h1>인기 노래방</h1>
      <HotNoraebangList />
    </div>
  );
}
