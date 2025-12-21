import { useState } from 'react';

import ProductCard from '../components/common/ProductCard.tsx';
import { getCoffees } from '../services/main.api.ts';
import type { ProductSummary } from '../types/ProductsType.ts';
import style from './HomePage.module.css';

function HomePage() {
    const [products, setProducts] = useState<ProductSummary[]>([]);

    useState(() => {
        const fetchedProducts = getCoffees();
        setProducts(fetchedProducts);
    });

    return (
        <div className={style.container}>
            <div className={style.homePage}>
                {products.map((product) => (
                    <ProductCard key={product.id} data={product} />
                ))}
            </div>
        </div>
    );
}

export default HomePage;
