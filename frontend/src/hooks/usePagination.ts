import { useEffect, useMemo, useState } from 'react';

import type { CartItem } from '../context/CartContext.tsx';
import type { ProductSummary } from '../types/ProductsType.ts';

interface PaginationProps {
    data: ProductSummary[] | CartItem[];
    itemsPerPage?: number;
    storageKey: 'homePage' | 'favoritesPage' | 'cartPage';
}

export function useClientPagination(props: PaginationProps) {
    const { data, itemsPerPage = 12, storageKey = 'homePage' } = props;
    const safeData = useMemo(() => {
        return Array.isArray(data) ? data : [];
    }, [data]);

    const [currentPage, setCurrentPage] = useState(() => {
        return Number(localStorage.getItem(storageKey)) || 1;
    });

    const totalPages = Math.max(1, Math.ceil(safeData.length / itemsPerPage));

    useEffect(() => {
        // reset to page 1 if current page exceeds total pages
        if (currentPage > totalPages && safeData.length > 0) {
            setCurrentPage(1);
        }
    }, [currentPage, totalPages, safeData.length]);

    useEffect(() => {
        localStorage.setItem(storageKey, String(currentPage));
    }, [currentPage, storageKey]);

    const paginationData = useMemo(() => {
        if (safeData.length === 0) return [];
        const start = (currentPage - 1) * itemsPerPage;
        return safeData.slice(start, start + itemsPerPage);
    }, [safeData, currentPage, itemsPerPage]);

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

interface BackendPaginationProps {
    storageKey: string;
    itemsPerPage?: number;
}

export function usePagination(props: BackendPaginationProps) {
    const { storageKey, itemsPerPage = 12 } = props;

    const [currentPage, setCurrentPage] = useState(() => {
        return Number(localStorage.getItem(storageKey)) || 1;
    });

    useEffect(() => {
        localStorage.setItem(storageKey, String(currentPage));
    }, [currentPage, storageKey]);

    const goToPage = (page: number) => {
        if (page < 1) {
            setCurrentPage(1);
        } else {
            setCurrentPage(page);
        }
    };

    return {
        currentPage,
        goToPage,
        itemsPerPage,
    };
}

