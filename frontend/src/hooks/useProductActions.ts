import type React from 'react';
import { useContext } from 'react';

import { CartContext, type CartContextType } from '../context/CartContext.tsx';
import { FavoritesContext, type FavoritesContextType } from '../context/FavoritesContext.tsx';
import type { ProductSummary } from '../types/ProductsType.ts';

export const useProductActions = (product: ProductSummary) => {
    const { favorites, toggleFavorite } = useContext<FavoritesContextType>(FavoritesContext);
    const { carts, toggleCart } = useContext<CartContextType>(CartContext);

    if (!product) {
        return {
            isInCart: false,
            isInFavorites: false,
            handleAddToCart: () => {
                /* intentional no-op */
            },
            handleAddToFavorites: () => {
                /* intentional no-op */
            },
        };
    }

    const isInCart = carts.some((item: ProductSummary) => item.id === product.id);
    const handleAddToCart = (e: React.MouseEvent) => {
        e.preventDefault();
        e.stopPropagation();
        toggleCart(product);
    };

    const isFavorite = favorites.some((fav: ProductSummary) => fav.id === product.id);
    const handleAddToFavorites = (e: React.MouseEvent) => {
        e.preventDefault();
        e.stopPropagation();
        toggleFavorite(product);
    };

    return {
        isInCart: isInCart,
        isInFavorites: isFavorite,
        handleAddToCart: handleAddToCart,
        handleAddToFavorites: handleAddToFavorites,
    };
};
