import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import './index.css';
import '@/shared/bass.css';
import { appRouter } from './appRouter';

const rootElement = document.getElementById('root') as HTMLElement;
const root = ReactDOM.createRoot(rootElement);

root.render(<RouterProvider router={appRouter()} />);
