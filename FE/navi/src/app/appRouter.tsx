import { createBrowserRouter } from 'react-router-dom';
import Layout from './layout';

import { CoverBoardPage } from '@/pages/coverBoard';
import { CoverDetailPage } from '@/pages/coverDetail';
import { CoverPostPage } from '@/pages/coverPost';
import { HomePage } from '@/pages/home';
import { ProfilePage } from '@/pages/profile';
import { SearchResultsPage } from '@/pages/searchResults';
import { SearchResultsDetailPage } from '@/pages/searchResultsDetail';
import { NoraebangBoardPage } from '@/pages/noraebangBoard';
import { NoraebangDetailPage } from '@/pages/noraebangDetail';
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
      path: '/noraebang/:noraebangPk',
      element: (
        <Layout>
          <NoraebangDetailPage />
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
      path: '/cover/:coverPk',
      element: (
        <Layout>
          <CoverDetailPage />
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
