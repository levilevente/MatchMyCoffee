import { useMemo } from 'react';

import type { CartItem } from '../../context/CartContext.tsx';
import { useClientPagination } from '../../hooks/usePagination.ts';
import type { ProductSummary } from '../../types/ProductsType.ts';
import ProductCard from '../common/ProductCard.tsx';
import ProductPagination from '../layout/ProductPagination.tsx';
import style from './ProductGrid.module.css';

interface Props {
    products: CartItem[] | ProductSummary[];
    inCart?: boolean;
    storageKey?: 'favoritesPage' | 'cartPage';
    currentPage?: number;
    totalPages?: number;
    onPageChange?: (page: number) => void;
}

const ProductGrid = (props: Props) => {
    const { inCart, products, storageKey, currentPage: externalCurrentPage, totalPages: externalTotalPages, onPageChange } = props;
    const safeProducts = Array.isArray(products) ? products : [];

    const clientPagination = storageKey ? useClientPagination({
        data: safeProducts,
        storageKey,
    }) : null;

    const productsToDisplay = useMemo(() => {
        if (storageKey && clientPagination) {
            return clientPagination.paginationData;
        }
        return safeProducts;
    }, [storageKey, clientPagination, safeProducts]);

    const showPagination = storageKey || (externalTotalPages && externalTotalPages > 1);

    return (
        <div className={style.container}>
            <div className={style.containerPage}>
                {productsToDisplay.map((item) => {
                    const productData = 'product' in item ? item.product : item;
                    return <ProductCard data={productData} key={productData.id} inCart={inCart} />;
                })}
            </div>
            {showPagination && (
                <ProductPagination
                    currentPage={storageKey && clientPagination ? clientPagination.currentPage : (externalCurrentPage || 1)}
                    goToPage={storageKey && clientPagination ? clientPagination.goToPage : (onPageChange || (() => {}))}
                    totalPages={storageKey && clientPagination ? clientPagination.totalPages : (externalTotalPages || 1)}
                />
            )}
        </div>
    );
};

export default ProductGrid;
