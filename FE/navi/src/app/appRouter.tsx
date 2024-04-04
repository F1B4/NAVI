import { useEffect, ReactNode } from 'react';
import { createBrowserRouter, useNavigate } from 'react-router-dom';
import Layout from './layout';
import { useUserStore } from '@/shared/store';

import { CoverBoardPage } from '@/pages/coverBoard';
import { CoverPostPage } from '@/pages/coverPost';
import { HomePage } from '@/pages/home';
import { ProfilePage } from '@/pages/profile';
import { SearchResultsPage } from '@/pages/searchResults';
import { SearchResultsDetailPage } from '@/pages/searchResultsDetail';
import { NoraebangBoardPage } from '@/pages/noraebangBoard';
import { NoraebangPostPage } from '@/pages/noraebangPost';

interface LoginGuardProps {
  children: ReactNode;
}

function LoginGuard({ children }: LoginGuardProps): JSX.Element {
  const store = useUserStore();
  const navi = useNavigate();

  useEffect(() => {
    if (!store.isLogin) {
      window.alert('로그인이 필요합니다');
      navi(-1);
    }
  }, []);
  return <>{children}</>;
}

function ModelGuard({ children }: LoginGuardProps): JSX.Element {
  const store = useUserStore();
  const navi = useNavigate();

  useEffect(() => {
    if (store.role === 'ROLE_GUEST') {
      window.alert(
        'AI 모델이 필요합니다.\nAI 모델은 10곡 이상 녹음 시 생성할 수 있습니다.',
      );
      navi(-1);
    }
  }, []);
  return <>{children}</>;
}

export const appRouter = () =>
  createBrowserRouter([
    {
      path: '/',
      element: (
        <Layout>
          <HomePage />
        </Layout>
      ),
    },
    {
      path: '/profile/:userPk',
      element: (
        <Layout>
          <ProfilePage />
        </Layout>
      ),
    },
    {
      path: '/search/:keyword',
      element: (
        <Layout>
          <SearchResultsPage />
        </Layout>
      ),
    },
    {
      path: '/search/:type/:keyword',
      element: (
        <Layout>
          <SearchResultsDetailPage />
        </Layout>
      ),
    },
    {
      path: '/noraebang',
      element: (
        <Layout>
          <NoraebangBoardPage />
        </Layout>
      ),
    },
    {
      path: '/noraebang/post',
      element: (
        <LoginGuard>
          <ModelGuard>
            <Layout>
              <NoraebangPostPage />
            </Layout>
          </ModelGuard>
        </LoginGuard>
      ),
    },
    {
      path: '/cover',
      element: (
        <Layout>
          <CoverBoardPage />
        </Layout>
      ),
    },
    {
      path: '/cover/post',
      element: (
        <LoginGuard>
          <ModelGuard>
            <Layout>
              <CoverPostPage />
            </Layout>
          </ModelGuard>
        </LoginGuard>
      ),
    },
  ]);
