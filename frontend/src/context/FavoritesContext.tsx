import { createContext, type ReactNode, useEffect, useState } from 'react';

import type { ProductSummary } from '../types/ProductsType.ts';

export interface FavoritesContextType {
    favorites: ProductSummary[];
    toggleFavorite: (product: ProductSummary) => void;
}

export const FavoritesContext = createContext<FavoritesContextType>({
    favorites: [],
    toggleFavorite: () => {
        /* intentional no-op */
    },
});

interface FavoritesProviderProps {
    children: ReactNode;
}

export function FavoritesProvider({ children }: FavoritesProviderProps) {
    const [favorites, setFavorites] = useState<ProductSummary[]>(() => {
        const saved = localStorage.getItem('my-coffee-favorites');
        if (!saved) return [];
        try {
            const parsed: unknown = JSON.parse(saved);
            if (Array.isArray(parsed)) {
                return parsed as ProductSummary[];
            }
        } catch {
            return [];
        }
        return [];
    });

    useEffect(() => {
        localStorage.setItem('my-coffee-favorites', JSON.stringify(favorites));
    }, [favorites]);

    const toggleFavorite = (product: ProductSummary) => {
        setFavorites((prevFavorites) => {
            const isFavorite = prevFavorites.some((fav) => fav.id === product.id);
            if (isFavorite) {
                return prevFavorites.filter((fav) => fav.id !== product.id);
            } else {
                return [...prevFavorites, product];
            }
        });
    };

    return <FavoritesContext.Provider value={{ favorites, toggleFavorite }}>{children}</FavoritesContext.Provider>;
}
