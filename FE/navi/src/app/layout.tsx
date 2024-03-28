import { LayoutHeader } from '@/widgets/LayoutHeader';
import { LayoutSidebar } from '@/widgets/LayoutSidebar';

import React from 'react';

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
  </div>
);

export default Layout;
