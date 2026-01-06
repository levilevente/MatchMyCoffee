import { useState } from 'react';

import ProductGrid from '../components/common/ProductGrid.tsx';
import { getCoffees } from '../services/main.api.ts';
import type { ProductSummary } from '../types/ProductsType.ts';
import style from './HomePage.module.css';

function HomePage() {
    const [products, setProducts] = useState<ProductSummary[]>([]);

    if (products.length === 0) {
        const fetchedProducts = getCoffees();
        setProducts(fetchedProducts);
    }

    return (
        <div className={style.container}>
            <ProductGrid products={products} storageKey={'homePage'} />
        </div>
    );
}

export default HomePage;
