import {createContext, type ReactNode, useEffect, useState} from "react";
import type {ProductSummary} from "../types/ProductsType.ts";

interface CartContextType {
    carts: ProductSummary[];
    toggleCart: (product: ProductSummary) => void;
}

export const CartContext = createContext<CartContextType>({
    carts: [],
    toggleCart: () => {
    },
});

interface CartProviderProps {
    children: ReactNode;
}

export function CartProvider({ children }: CartProviderProps) {
    const [carts, setCarts] = useState<ProductSummary[]>(() => {
        const saved = localStorage.getItem("my-coffee-cart");
        return saved ? JSON.parse(saved) : [];
    });

    useEffect(() => {
        localStorage.setItem("my-coffee-cart", JSON.stringify(carts));
    })

    const toggleCart = (product: ProductSummary) => {
        setCarts((prevState) => {
            const isInCart = prevState.some((item) => item.id === product.id);
            if (isInCart) {
                return prevState.filter((item) => item.id !== product.id);
            } else {
                return [...prevState, product];
            }
        });
    }

    return (
        <CartContext.Provider value={{carts, toggleCart}}>
            {children}
        </CartContext.Provider>
    );
}