import { useContext } from 'react';
import { useTranslation } from 'react-i18next';

import EmptyState from '../components/common/EmptyState.tsx';
import ProductGrid from '../components/common/ProductGrid.tsx';
import { CartContext } from '../context/CartContext.tsx';

function CartPage() {
    const { carts } = useContext(CartContext);
    const { t } = useTranslation();

    if (carts.length === 0) {
        return <EmptyState title={t('cart.emptyTitleMessage')} subtitle={t('cart.emptySubtitleMessage')} />;
    } else {
        return <ProductGrid products={carts} inCart={true}/>;
    }
}

export default CartPage;
