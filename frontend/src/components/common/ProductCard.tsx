import { Button, ButtonGroup, Card, Dropdown, DropdownButton } from 'react-bootstrap';
import { IoCloseSharp } from 'react-icons/io5';
import { Link } from 'react-router-dom';

import { useProductActions } from '../../hooks/useProductActions.ts';
import type { ProductSummary } from '../../types/ProductsType.ts';
import AddToCartButton from '../common/AddToCartButton.tsx';
import AddToFavoritesButton from '../common/AddToFavoritesButton.tsx';
import StarRating from '../common/StarRating.tsx';
import style from './ProductCard.module.css';

interface ProductTypeProps {
    data: ProductSummary;
    inCart?: boolean;
}

function ProductCard(props: ProductTypeProps) {
    const { data, inCart } = props;
    const { currentQuantity, handleQuantityChange, removeFromCart } = useProductActions(data);

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
                    <Card.Title>{data.name}</Card.Title>
                    <Card.Text>{data.price} $</Card.Text>
                    <div>
                        <StarRating rating={data.averageRating} showCount={data.reviewCount} />
                    </div>
                </Link>
                {inCart ? (
                    <>
                        <DropdownButton
                            as={ButtonGroup}
                            key={'cart-count'}
                            id={`cart-count`}
                            variant={'none'}
                            title={currentQuantity}
                            onSelect={handleQuantitySelect}
                        >
                            {Array.from({ length: 10 }, (_, i) => i + 1).map((count) => (
                                <Dropdown.Item key={count} eventKey={count.toString()}>
                                    {count}
                                </Dropdown.Item>
                            ))}
                        </DropdownButton>
                    </>
                ) : (
                    <AddToCartButton product={data} variant={'icon-only'} inCart={inCart} />
                )}
                <AddToFavoritesButton product={data} variant={'icon-only'} />
            </Card.Body>
        </Card>
    );
}

export default ProductCard;
