// Layout 컴포넌트 파일
import React from 'react';
import { LayoutHeader } from '@/widgets/LayoutHeader';
import { LayoutSidebar } from '@/widgets/LayoutSidebar';
import { PlayBar } from '@/widgets/Playbar'; // PlayBar 컴포넌트 임포트

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => (
  <div style={{ maxWidth: '100vw', overflow: 'hidden' }}>
    <LayoutHeader />
    <div style={{ display: 'flex', paddingTop: '75px' }}>
      <LayoutSidebar />
      <div style={{ flex: 1, marginLeft: '320px' }}>{children}</div>
    </div>
    <PlayBar url="https://navi.s3.ap-northeast-2.amazonaws.com/%EC%9A%B0%EB%A6%AC%EC%9D%98%EA%BF%88(%EA%B9%80%EC%9D%B5%ED%99%98%2C%ED%99%A9%EC%9C%A4%EC%A0%95)_mr%EC%A1%B0%EC%A0%95.mp4" />
  </div>
);

export default Layout;
