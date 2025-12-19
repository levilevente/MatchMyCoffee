import './index.css';
import './i18n';

import { createRoot } from 'react-dom/client';

import Root from './Root.tsx';

createRoot(document.getElementById('root')!).render(<Root />);
