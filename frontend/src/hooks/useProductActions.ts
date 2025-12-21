import React, {useContext} from "react";
import {FavoritesContext} from "../context/FavoritesContext.tsx";
import type {ProductSummary} from "../types/ProductsType.ts";
import {CartContext} from "../context/CartContext.tsx";

export const useProductActions = (product: ProductSummary) => {
    const {favorites, toggleFavorite} = useContext<any>(FavoritesContext);
    const {carts, toggleCart} = useContext<any>(CartContext);

    if (!product) {
        return {
            isInCart: false,
            isInFavorites: false,
            handleAddToCart: () => {},
            handleAddToFavorites: () => {},
        };
    }

    const isInCart = carts.some((item: any) => item.id === product.id);
    const handleAddToCart = (e: React.MouseEvent) => {
        e.preventDefault();
        e.stopPropagation();
        toggleCart(product);
    };

    const isFavorite = favorites.some((fav: any) => fav.id === product.id);
    const handleAddToFavorites = (e: React.MouseEvent) => {
        e.preventDefault();
        e.stopPropagation();
        toggleFavorite(product);
    }

    return {
        isInCart: isInCart,
        isInFavorites: isFavorite,
        handleAddToCart: handleAddToCart,
        handleAddToFavorites: handleAddToFavorites,
    };
}