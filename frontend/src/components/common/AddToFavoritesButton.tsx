import { Button } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { FaHeart } from 'react-icons/fa';

import { useProductActions } from '../../hooks/useProductActions.ts';
import type { ProductSummary } from '../../types/ProductsType.ts';

interface AddToFavoritesButtonProps {
    product: ProductSummary;
    variant?: 'icon-only' | 'with-text';
}

function AddToFavoritesButton(props: AddToFavoritesButtonProps) {
    const { product, variant = 'icon-only' } = props;
    const { isInFavorites, handleAddToFavorites } = useProductActions(product);
    const { t } = useTranslation();

    if (variant === 'with-text') {
        return (
            <Button variant={isInFavorites ? 'danger' : 'dark'} onClick={handleAddToFavorites}>
                <FaHeart /> {isInFavorites ? t('product.removeFromFavorites') : t('product.addToFavorites')}
            </Button>
        );
    }

    if (variant === 'icon-only') {
        return (
            <Button variant="none" onClick={handleAddToFavorites}>
                {isInFavorites ? <FaHeart color="red" /> : <FaHeart />}
            </Button>
        );
    }
}

export default AddToFavoritesButton;
