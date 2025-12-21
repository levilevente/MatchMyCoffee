import {createContext, type ReactNode, useEffect, useState} from "react";
import type {ProductSummary} from "../types/ProductsType.ts";

interface FavoritesContextType {
    favorites: ProductSummary[];
    toggleFavorite: (product: ProductSummary) => void;
}

export const FavoritesContext = createContext<FavoritesContextType>({
    favorites: [],
    toggleFavorite: () => {
    },
});

interface FavoritesProviderProps {
    children: ReactNode;
}

export function FavoritesProvider({ children }: FavoritesProviderProps) {
    const [favorites, setFavorites] = useState<ProductSummary[]>(() => {
        const saved = localStorage.getItem("my-coffee-favorites");
        return saved ? JSON.parse(saved) : [];
    });

    useEffect(() => {
        localStorage.setItem("my-coffee-favorites", JSON.stringify(favorites));
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
    }

    return (
        <FavoritesContext.Provider value={{favorites, toggleFavorite}}>
            {children}
        </FavoritesContext.Provider>
    );
}