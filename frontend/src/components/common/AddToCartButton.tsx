import { Button } from 'react-bootstrap';
import { FaCartShopping } from 'react-icons/fa6';

import { useProductActions } from '../../hooks/useProductActions.ts';
import type { ProductSummary } from '../../types/ProductsType.ts';

interface AddToCartButtonProps {
    product: ProductSummary;
    variant?: 'icon-only' | 'with-text';
}

function AddToCartButton(props: AddToCartButtonProps) {
    const { product, variant = 'icon-only' } = props;

    const { isInCart, handleAddToCart } = useProductActions(product);

    if (variant === 'with-text') {
        return (
            <Button variant={isInCart ? 'success' : 'dark'} onClick={handleAddToCart} disabled={isInCart}>
                <FaCartShopping /> {isInCart ? 'Added to cart' : 'Add to cart'}
            </Button>
        );
    }

    if (variant === 'icon-only') {
        return (
            <Button variant="none" onClick={handleAddToCart}>
                <FaCartShopping color={isInCart ? 'green' : 'black'} />
            </Button>
        );
    }
}

export default AddToCartButton;
