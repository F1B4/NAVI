// import { Layout } from '@/shared/ui
import { LayoutHeader } from '@/widgets/LayoutHeader';
import { LayoutSidebar } from '@/widgets/LayoutSidebar';

import React from 'react';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => (
  <div>
    <LayoutHeader />
    <div style={{ display: 'flex' }}>
      <LayoutSidebar />
      <div style={{ flex: 1, marginLeft: '80px' }}>{children}</div>
    </div>
  </div>
);

export default Layout;
