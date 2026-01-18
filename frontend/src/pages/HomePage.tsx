import ProductGrid from '../components/common/ProductGrid.tsx';
import { usePagination } from '../hooks/usePagination.ts';
import { useAllProducts } from '../query/main.query.ts';
import style from './HomePage.module.css';

function HomePage() {
    const { currentPage, itemsPerPage, goToPage } = usePagination({
        storageKey: 'homePage',
    });

    const { data, isLoading, error } = useAllProducts(currentPage - 1, itemsPerPage);

    const products = data?.content ?? [];
    const totalPages = data?.totalPages ?? 1;

    if (isLoading) {
        return <div className={style.container}>Loading...</div>;
    }

    if (error) {
        return <div className={style.container}>Error loading products.</div>;
    }

    return (
        <div className={style.container}>
            <ProductGrid
                products={products}
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={goToPage}
            />
        </div>
    );
}

export default HomePage;
