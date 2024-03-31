// Layout 컴포넌트 파일
import React from 'react';
import { LayoutHeader } from '@/widgets/LayoutHeader';
import { LayoutSidebar } from '@/widgets/LayoutSidebar';
import { PlayBar } from '@/widgets/Playbar';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => (
  <div style={{ maxWidth: '100vw', overflow: 'hidden' }}>
    <div style={{ position: 'relative', zIndex: 20 }}>
      <LayoutHeader />
    </div>
    <div style={{ display: 'flex', paddingTop: '75px' }}>
      <LayoutSidebar />
      <div style={{ flex: 1, marginLeft: '320px' }}>{children}</div>
    </div>
    <PlayBar
      url="https://navi.s3.ap-northeast-2.amazonaws.com/%EC%9A%B0%EB%A6%AC%EC%9D%98%EA%BF%88(%EA%B9%80%EC%9D%B5%ED%99%98%2C%ED%99%A9%EC%9C%A4%EC%A0%95)_mr%EC%A1%B0%EC%A0%95.mp4"
      title="우리의 꿈"
      coverImage="https://mblogthumb-phinf.pstatic.net/MjAyMTAzMDhfMzQg/MDAxNjE1MTk5Mjc0Njcw.ytGfRefugdGDmIQ0618i8G40DSuDmD5aO74emJSdwrMg.N-8_e9rwHECvsiHGXCda18uhN3r0Aky_OCYw9gZgrD4g.JPEG.kcy514/IMG_1739.JPG?type=w800"
      artist="김익환, 황윤정"
    />
  </div>
);

export default Layout;
