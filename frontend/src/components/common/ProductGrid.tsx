import { useMemo } from 'react';

import type { CartItem } from '../../context/CartContext.tsx';
import { usePagination } from '../../hooks/usePagination.ts';
import type { ProductSummary } from '../../types/ProductsType.ts';
import ProductCard from '../common/ProductCard.tsx';
import ProductPagination from '../layout/ProductPagination.tsx';
import style from './ProductGrid.module.css';

interface Props {
    products: CartItem[] | ProductSummary[];
    inCart?: boolean;
    storageKey?: 'homePage' | 'favoritesPage' | 'cartPage';
}

const ProductGrid = (props: Props) => {
    const { inCart, products, storageKey } = props;
    const { currentPage, itemsPerPage, goToPage, totalPages } = usePagination({
        data: products,
        storageKey: storageKey ?? 'homePage',
    });

    const productsToDisplay = useMemo(() => {
        const start = (currentPage - 1) * itemsPerPage;
        return products.slice(start, start + itemsPerPage);
    }, [products, currentPage, itemsPerPage]);

    if (!storageKey) {
        return (
            <div className={style.container}>
                <div className={style.containerPage}>
                    {products.map((item) => {
                        const productData = 'product' in item ? item.product : item;
                        return <ProductCard data={productData} key={productData.id} inCart={inCart} />;
                    })}
                </div>
            </div>
        );
    }

    return (
        <div className={style.container}>
            <div className={style.containerPage}>
                {productsToDisplay.map((item) => {
                    const productData = 'product' in item ? item.product : item;
                    return <ProductCard data={productData} key={productData.id} inCart={inCart} />;
                })}
            </div>
            <ProductPagination currentPage={currentPage} goToPage={goToPage} totalPages={totalPages} />
        </div>
    );
};

export default ProductGrid;
