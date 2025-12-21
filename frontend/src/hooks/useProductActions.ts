import type React from 'react';
import { useContext } from 'react';

import { CartContext, type CartContextType } from '../context/CartContext.tsx';
import { FavoritesContext, type FavoritesContextType } from '../context/FavoritesContext.tsx';
import type { ProductSummary } from '../types/ProductsType.ts';

export const useProductActions = (product: ProductSummary) => {
    const { favorites, toggleFavorite } = useContext<FavoritesContextType>(FavoritesContext);
    const { cartItems, addToCart, removeFromCart, updateQuantity } = useContext<CartContextType>(CartContext);

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
            currentQuantity: 0,
            handleQuantityChange: () => {
                /* intentional no-op */
            },
            removeFromCart: () => {
                /* intentional no-op */
            },
        };
    }

    const existingCartItem = cartItems.find((item) => item.product.id === product.id);
    const isInCart = !!existingCartItem;
    const currentQuantity = existingCartItem?.quantity ?? 0;
    const handleAddToCart = (e: React.MouseEvent) => {
        e.preventDefault();
        e.stopPropagation();
        if (isInCart) {
            removeFromCart(product.id);
        } else {
            addToCart(product, 1);
        }
    };

    const isFavorite = favorites.some((fav: ProductSummary) => fav.id === product.id);
    const handleAddToFavorites = (e: React.MouseEvent) => {
        e.preventDefault();
        e.stopPropagation();
        toggleFavorite(product);
    };

    const handleQuantityChange = (newQuantity: number) => {
        if (newQuantity <= 0) {
            removeFromCart(product.id);
        } else {
            updateQuantity(product.id, newQuantity);
        }
    };

    return {
        isInCart: isInCart,
        isInFavorites: isFavorite,
        currentQuantity,
        handleQuantityChange: handleQuantityChange,
        handleAddToCart: handleAddToCart,
        handleAddToFavorites: handleAddToFavorites,
        removeFromCart: () => removeFromCart(product.id),
    };
};
