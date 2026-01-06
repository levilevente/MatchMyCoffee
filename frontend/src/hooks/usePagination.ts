import { useEffect, useMemo, useState } from 'react';

import type { CartItem } from '../context/CartContext.tsx';
import type { ProductSummary } from '../types/ProductsType.ts';

interface PaginationProps {
    data: ProductSummary[] | CartItem[];
    itemsPerPage?: number;
    storageKey: 'homePage' | 'favoritesPage' | 'cartPage';
}

export function usePagination(props: PaginationProps) {
    const { data = [], itemsPerPage = 12, storageKey = 'homePage' } = props;
    const [currentPage, setCurrentPage] = useState(() => {
        return Number(localStorage.getItem(storageKey)) || 1;
    });

    useEffect(() => {
        localStorage.setItem(storageKey, String(currentPage));
    }, [currentPage, storageKey]);

    const totalPages = Math.ceil(data.length / itemsPerPage);

    const paginationData = useMemo(() => {
        const start = (currentPage - 1) * itemsPerPage;
        return data.slice(start, start + itemsPerPage);
    }, [data, currentPage, itemsPerPage]);

    const goToPage = (page: number) => {
        if (page < 1) {
            setCurrentPage(1);
        } else if (page > totalPages) {
            setCurrentPage(totalPages);
        } else {
            setCurrentPage(page);
        }
    };

    return {
        currentPage,
        goToPage,
        totalPages,
        paginationData,
        itemsPerPage,
    };
}
