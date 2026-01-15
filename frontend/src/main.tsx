import './index.css';
import './i18n';

import { QueryClientProvider } from '@tanstack/react-query';
import { createRoot } from 'react-dom/client';

import { CartProvider } from './context/CartContext.tsx';
import { FavoritesProvider } from './context/FavoritesContext.tsx';
import { queryClient } from './query/common.query.ts';
import Root from './Root.tsx';

createRoot(document.getElementById('root')!).render(
    <QueryClientProvider client={queryClient}>
        <FavoritesProvider>
            <CartProvider>
                <Root />
            </CartProvider>
        </FavoritesProvider>
        ,
    </QueryClientProvider>,
);
