import { createContext, type ReactNode, useEffect, useState } from 'react';

import type { ProductSummary } from '../types/ProductsType.ts';

export interface CartContextType {
    carts: ProductSummary[];
    toggleCart: (product: ProductSummary) => void;
}

export const CartContext = createContext<CartContextType>({
    carts: [],
    toggleCart: () => {
        /* intentional no-op */
    },
});

interface CartProviderProps {
    children: ReactNode;
}

export function CartProvider({ children }: CartProviderProps) {
    const [carts, setCarts] = useState<ProductSummary[]>(() => {
        const saved: string | null = localStorage.getItem('my-coffee-cart');
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
        localStorage.setItem('my-coffee-cart', JSON.stringify(carts));
    });

    const toggleCart = (product: ProductSummary) => {
        setCarts((prevState) => {
            const isInCart = prevState.some((item) => item.id === product.id);
            if (isInCart) {
                return prevState.filter((item) => item.id !== product.id);
            } else {
                return [...prevState, product];
            }
        });
    };

    return <CartContext.Provider value={{ carts, toggleCart }}>{children}</CartContext.Provider>;
}
