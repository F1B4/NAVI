// import { Layout } from '@/shared/ui
import { LayoutHeader } from '@/widgets/LayoutHeader';
// import { LayoutProfileCard } from '@/widgets/LayoutProfileCard'

import React from 'react';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => (
  <div>
    <LayoutHeader />
    {children}
  </div>
);

export default Layout;
