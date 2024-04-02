import { createBrowserRouter } from 'react-router-dom';
import Layout from './layout';

import { CoverBoardPage } from '@/pages/coverBoard';
import { CoverPostPage } from '@/pages/coverPost';
import { HomePage } from '@/pages/home';
import { ProfilePage } from '@/pages/profile';
import { SearchResultsPage } from '@/pages/searchResults';
import { SearchResultsDetailPage } from '@/pages/searchResultsDetail';
import { NoraebangBoardPage } from '@/pages/noraebangBoard';
import { NoraebangPostPage } from '@/pages/noraebangPost';

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
      path: '/search/detail/:keyword',
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
        <Layout>
          <NoraebangPostPage />
        </Layout>
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
        <Layout>
          <CoverPostPage />
        </Layout>
      ),
    },
  ]);
