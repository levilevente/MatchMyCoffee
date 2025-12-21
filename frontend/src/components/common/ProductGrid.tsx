import type { ProductSummary } from '../../types/ProductsType.ts';
import ProductCard from '../homepage/ProductCard.tsx';
import style from './ProductGrid.module.css';

interface Props {
    products: ProductSummary[];
}

const ProductGrid = ({ products }: Props) => {
    return (
        <div className={style.container}>
            <div className={style.containerPage}>
                {products.map((product) => (
                    <ProductCard data={product} key={product.id} />
                ))}
            </div>
        </div>
    );
};

export default ProductGrid;
