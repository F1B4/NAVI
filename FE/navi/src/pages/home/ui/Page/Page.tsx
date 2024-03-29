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

  const noti = () => {
    axios.get(`${baseApi}/notification/subscribe/${store.userId}`);
  };

  useEffect(() => {
    const fetchData = async () => {
      if (loginSuccess && store.userId === 0) {
        store.getData();
        console.log('check');
        noti();
        console.log('check2');
      }
    };
    fetchData();
  });

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
