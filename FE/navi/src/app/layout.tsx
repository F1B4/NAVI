import { LayoutHeader } from '@/widgets/LayoutHeader';
import { LayoutSidebar } from '@/widgets/LayoutSidebar';
import { PlayBar } from '@/widgets/Playbar';
import { usePlayStore } from '@/shared/store/playStore/playStore';

interface LayoutProps {
  children: React.ReactNode;
}

const PlayBarContainer: React.FC = () => {
  const store = usePlayStore();

  return (
    <PlayBar
      type={store.type}
      url={store.url}
      title={store.title}
      coverImage={store.coverImage}
      artist={store.artist}
    />
  );
};

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div style={{ maxWidth: '100vw', overflow: 'hidden' }}>
      <div style={{ position: 'relative', zIndex: 20 }}>
        <LayoutHeader />
      </div>
      <div style={{ display: 'flex', paddingTop: '75px' }}>
        <LayoutSidebar />
        <div style={{ flex: 1, marginLeft: '320px' }}>{children}</div>
      </div>
      <PlayBarContainer />
    </div>
  );
};

export default Layout;
