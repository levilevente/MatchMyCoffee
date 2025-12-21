import {Button} from 'react-bootstrap';
import {FaCartShopping} from 'react-icons/fa6';

import {useProductActions} from '../../hooks/useProductActions.ts';
import type {ProductSummary} from '../../types/ProductsType.ts';
import {useTranslation} from "react-i18next";

interface AddToCartButtonProps {
    product: ProductSummary;
    variant?: 'icon-only' | 'with-text';
    inCart?: boolean;
}

function AddToCartButton(props: AddToCartButtonProps) {
    const {product, variant = 'icon-only', inCart} = props;
    const {isInCart, handleAddToCart} = useProductActions(product);
    const {t} = useTranslation();

    if (variant === 'with-text') {
        return (
            <Button variant={isInCart ? 'success' : 'dark'} onClick={handleAddToCart} disabled={isInCart}>
                <FaCartShopping/> {isInCart ? t("product.addedToCart") : t("product.addToCart")}
            </Button>
        );
    }

    if (variant === 'icon-only') {
        if (inCart) {
            return (<Button variant="none" onClick={handleAddToCart} className="border-0">
                <FaCartShopping color={isInCart ? 'green' : 'black'}/>
            </Button>)
        } else {
            return (<Button variant="none" onClick={handleAddToCart} disabled={isInCart} className="border-0">
                <FaCartShopping color={isInCart ? 'green' : 'black'}/>
            </Button>)
        }
    }
}

export default AddToCartButton;
