import type { CartItem } from '../../context/CartContext.tsx';
import type { ProductSummary } from '../../types/ProductsType.ts';
import ProductCard from '../common/ProductCard.tsx';
import style from './ProductGrid.module.css';

interface Props {
    products: CartItem[] | ProductSummary[];
    inCart?: boolean;
}

const ProductGrid = (props: Props) => {
    const { inCart, products } = props;
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
};

export default ProductGrid;
