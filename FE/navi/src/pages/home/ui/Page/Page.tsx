import { useEffect } from 'react';
import { NewContentsList } from '../NewContentsList/NewContentsList';
import { HotCoverList } from '../HotCoverList/HotCoverList';
import { HotNoraebangList } from '../HotNoraebangList/HotNoraebangList';
import { useUserStore } from '@/shared/store';
import axios from 'axios';
import css from './Page.module.css';
import { baseApi } from '@/shared/api';

export function HomePage() {
  const store = useUserStore();
  const search = window.location.search;
  const params = new URLSearchParams(search);
  const loginSuccess = params.get('loginSuccess');

  useEffect(() => {
    const noti = async () => {
      await axios.get(`${baseApi}/sse/notification/subscribe/${store.userId}`, {
        headers: {
          Accept: 'text/event-stream',
          'Cache-Control': 'no-cache',
        },
      });
    };

    const fetchData = async () => {
      if (loginSuccess && store.userId === 0) {
        console.log('check');
        store.getData();
        await noti();
      }
    };

    const fetchRecordData = async () => {
      if (store.isLogin && store.role === 'ROLE_GUEST') {
        store.getRec();
      }
    };

    fetchData();
    fetchRecordData();
  }, []);

  return (
    <div className={css.root}>
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
