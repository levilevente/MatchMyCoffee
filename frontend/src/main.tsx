import './index.css';
import './i18n';

import { createRoot } from 'react-dom/client';

import { CartProvider } from './context/CartContext.tsx';
import { FavoritesProvider } from './context/FavoritesContext.tsx';
import Root from './Root.tsx';

createRoot(document.getElementById('root')!).render(
    <FavoritesProvider>
        <CartProvider>
            <Root />
        </CartProvider>
    </FavoritesProvider>,
);
