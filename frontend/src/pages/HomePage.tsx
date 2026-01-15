import ProductGrid from '../components/common/ProductGrid.tsx';
import { useAllProducts } from '../query/main.query.ts';
import style from './HomePage.module.css';

function HomePage() {
    const { data: products = [], isLoading, error } = useAllProducts();

    if (isLoading) {
        return <div className={style.container}>Loading...</div>;
    }

    if (error) {
        return <div className={style.container}>Error loading products.</div>;
    }

    return (
        <div className={style.container}>
            <ProductGrid products={products} storageKey={'homePage'} />
        </div>
    );
}

export default HomePage;
