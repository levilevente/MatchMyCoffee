import type { ProductSummary } from '../../types/ProductsType.ts';
import ProductCard from '../common/ProductCard.tsx';
import style from './ProductGrid.module.css';

interface Props {
    products: ProductSummary[];
    inCart?: boolean;
}

const ProductGrid = (props: Props) => {
    const {inCart, products} = props;
    return (
        <div className={style.container}>
            <div className={style.containerPage}>
                {products.map((product) => (
                    <ProductCard data={product} key={product.id} inCart={inCart} />
                ))}
            </div>
        </div>
    );
};

export default ProductGrid;
