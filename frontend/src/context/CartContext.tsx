import { createContext, type ReactNode, useEffect, useState } from 'react';

import type { ProductSummary } from '../types/ProductsType.ts';

export interface CartContextType {
    cartItems: CartItem[];
    addToCart: (product: ProductSummary, quantity?: number) => void;
    removeFromCart: (productId: number) => void;
    updateQuantity: (productId: number, newQuantity: number) => void;
    clearCart: () => void;
}

export interface CartItem {
    product: ProductSummary;
    quantity: number;
}

export const CartContext = createContext<CartContextType>({
    cartItems: [],
    addToCart: () => undefined,
    removeFromCart: () => undefined,
    updateQuantity: () => undefined,
    clearCart: () => undefined,
});

interface CartProviderProps {
    children: ReactNode;
}

export function CartProvider({ children }: CartProviderProps) {
    const [cartItems, setCartItems] = useState<CartItem[]>(() => {
        const saved: string | null = localStorage.getItem('my-coffee-cart');
        if (!saved) return [];

        const isProductSummary = (v: unknown): v is ProductSummary => {
            if (v && typeof v === 'object') {
                const obj = v as Record<string, unknown>;
                return typeof obj.id === 'number';
            }
            return false;
        };

        const isCartItem = (v: unknown): v is CartItem => {
            if (v && typeof v === 'object') {
                const obj = v as Record<string, unknown>;
                return isProductSummary(obj.product) && typeof obj.quantity === 'number';
            }
            return false;
        };

        const isCartItemArray = (v: unknown): v is CartItem[] => Array.isArray(v) && v.every(isCartItem);

        try {
            const parsed: unknown = JSON.parse(saved);
            if (isCartItemArray(parsed)) {
                return parsed;
            }
        } catch {
            return [];
        }
        return [];
    });

    useEffect(() => {
        localStorage.setItem('my-coffee-cart', JSON.stringify(cartItems));
    }, [cartItems]);

    const addToCart = (product: ProductSummary, quantity = 1) => {
        setCartItems((prevItems) => {
            const existingItem = prevItems.find((item) => item.product.id === product.id);

            if (existingItem) {
                // If exists, increment the quantity
                return prevItems.map((item) =>
                    item.product.id === product.id ? { ...item, quantity: item.quantity + quantity } : item,
                );
            } else {
                // If new, add to array
                return [...prevItems, { product, quantity }];
            }
        });
    };

    const removeFromCart = (productId: number) => {
        setCartItems((prevItems) => prevItems.filter((item) => item.product.id !== productId));
    };

    const updateQuantity = (productId: number, newQuantity: number) => {
        if (newQuantity < 1) {
            removeFromCart(productId); // Remove if quantity goes to 0
            return;
        }

        setCartItems((prevItems) =>
            prevItems.map((item) => (item.product.id === productId ? { ...item, quantity: newQuantity } : item)),
        );
    };

    const clearCart = () => {
        setCartItems([]);
    };

    return (
        <CartContext.Provider value={{ cartItems, addToCart, removeFromCart, updateQuantity, clearCart }}>
            {children}
        </CartContext.Provider>
    );
}
