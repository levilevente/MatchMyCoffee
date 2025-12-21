import { useContext } from 'react';
import { Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import EmptyState from '../components/common/EmptyState.tsx';
import ProductGrid from '../components/common/ProductGrid.tsx';
import { CartContext } from '../context/CartContext.tsx';
import style from './CartPage.module.css';

function CartPage() {
    const { cartItems } = useContext(CartContext);
    const { t } = useTranslation();

    const handleFinalizeOrder = () => {
        let finalPrice = 0;
        cartItems.forEach((cartItem) => {
            console.warn(`Product ID: ${cartItem.product.id}, Quantity: ${cartItem.quantity}`);
            finalPrice += cartItem.product.price * cartItem.quantity;
        });
        console.warn(`Final Price: $${finalPrice.toFixed(2)}`);
    };

    if (cartItems.length === 0) {
        return <EmptyState title={t('cart.emptyTitleMessage')} subtitle={t('cart.emptySubtitleMessage')} />;
    } else {
        return (
            <div className={style.container}>
                <Button variant={'none'} className={style.finalizeOrderButton} onClick={handleFinalizeOrder}>
                    Finalize Order
                </Button>
                <ProductGrid products={cartItems} inCart />
            </div>
        );
    }
}

export default CartPage;
