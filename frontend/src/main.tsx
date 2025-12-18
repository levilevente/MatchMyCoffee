import './index.css';
import './i18n';

import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';

import Root from './Root.tsx';

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <Root />
    </StrictMode>,
);
