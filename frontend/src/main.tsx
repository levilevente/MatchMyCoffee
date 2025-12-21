import './index.css';
import './i18n';

import {createRoot} from 'react-dom/client';

import Root from './Root.tsx';
import {FavoritesProvider} from "./context/FavoritesContext.tsx";
import {CartProvider} from "./context/CartContext.tsx";

createRoot(document.getElementById('root')!).render(
    <FavoritesProvider>
        <CartProvider>
            <Root/>
        </CartProvider>
    </FavoritesProvider>
);
