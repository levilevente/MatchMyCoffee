import { useContext } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router';

import EmptyState from '../components/common/EmptyState.tsx';
import MainButton from '../components/common/MainButton.tsx';
import ProductGrid from '../components/common/ProductGrid.tsx';
import { CartContext } from '../context/CartContext.tsx';
import style from './CartPage.module.css';

function CartPage() {
    const { cartItems } = useContext(CartContext);
    const { t } = useTranslation();
    const navigate = useNavigate();

    const handleFinalizeOrder = () => {
        void navigate('/cart/checkout');
    };

    if (cartItems.length === 0) {
        return <EmptyState title={t('cart.emptyTitleMessage')} subtitle={t('cart.emptySubtitleMessage')} />;
    } else {
        return (
            <div className={style.container}>
                <div className={style.buttonContainer}>
                    <MainButton text={t('cart.finalizePurchase')} onClick={handleFinalizeOrder} />
                </div>
                <ProductGrid products={cartItems} inCart storageKey={'cartPage'} />
            </div>
        );
    }
}

export default CartPage;
