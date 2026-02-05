import { Button, Card } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { IoCloseSharp } from 'react-icons/io5';
import { Link } from 'react-router-dom';

import { useProductActions } from '../../hooks/useProductActions.ts';
import type { ProductSummary } from '../../types/ProductsType.ts';
import StarRating from '../common/StarRating.tsx';
import CustomButtonGroup from '../product/CustomButtonGroup.tsx';
import style from './ProductCard.module.css';

interface ProductTypeProps {
    data: ProductSummary;
    inCart?: boolean;
}

function ProductCard(props: ProductTypeProps) {
    const { data, inCart } = props;
    const { currentQuantity, handleQuantityChange, removeFromCart } = useProductActions(data);
    const { t } = useTranslation();

    const handleQuantitySelect = (eventKey: string | null) => {
        if (eventKey) {
            const newQuantity: number = parseInt(eventKey, 10);
            handleQuantityChange(newQuantity);
        }
    };

    const handleRemove = () => {
        removeFromCart();
    };

    return (
        <Card style={{ width: '18rem' }}>
            <Card.Body>
                {inCart ? (
                    <Button variant={'none'} onClick={handleRemove}>
                        <IoCloseSharp />
                    </Button>
                ) : null}
                <Link
                    to={`/products/${data.id}`}
                    className="text-decoration-none text-reset"
                    aria-label={`View details for ${data.name}`}
                >
                    <Card.Img variant="top" src={data.imageUrl} className={style.image} />
                    <Card.Title>
                        <h3>{data.name}</h3>
                    </Card.Title>
                    <h3>{data.price} $</h3>
                    <div>
                        <StarRating rating={data.averageRating} showCount={data.reviewCount} key={data.id} />
                    </div>
                </Link>
                {}
                {data.isActive || data.stock <= 0 ? (
                    <div className={style.buttonGroup}>
                        <CustomButtonGroup
                            inCart={inCart}
                            data={data}
                            currentQuantity={currentQuantity}
                            handleQuantitySelect={handleQuantitySelect}
                        />
                    </div>
                ) : (
                    <div className={style.outOfStockLabel}>{t('product.outOfStock')}</div>
                )}
            </Card.Body>
        </Card>
    );
}

export default ProductCard;
