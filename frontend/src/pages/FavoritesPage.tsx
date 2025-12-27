import { useContext } from 'react';
import { useTranslation } from 'react-i18next';

import EmptyState from '../components/common/EmptyState.tsx';
import ProductGrid from '../components/common/ProductGrid.tsx';
import { FavoritesContext } from '../context/FavoritesContext.tsx';

function FavoritesPage() {
    const { favorites } = useContext(FavoritesContext);
    const { t } = useTranslation();

    if (favorites.length === 0) {
        return <EmptyState title={t('favorites.emptyTitleMessage')} subtitle={t('favorites.emptySubtitleMessage')} />;
    } else {
        return <ProductGrid products={favorites} storageKey={'favoritesPage'} />;
    }
}

export default FavoritesPage;
