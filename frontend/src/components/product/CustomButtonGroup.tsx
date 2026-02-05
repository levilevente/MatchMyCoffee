import { ButtonGroup, Dropdown, DropdownButton } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';

import type { ProductSummary } from '../../types/ProductsType.ts';
import AddToCartButton from '../common/AddToCartButton.tsx';
import AddToFavoritesButton from '../common/AddToFavoritesButton.tsx';

interface CustomButtonGroupProps {
    inCart?: boolean;
    data: ProductSummary;
    currentQuantity: number;
    handleQuantitySelect: (eventKey: string | null) => void;
}

function CustomButtonGroup({ inCart, data, currentQuantity, handleQuantitySelect }: CustomButtonGroupProps) {
    const { t } = useTranslation();

    return (
        <>
            {inCart ? (
                <DropdownButton
                    as={ButtonGroup}
                    key={'cart-count'}
                    id={`cart-count`}
                    variant={'none'}
                    title={`${t('product.quantity')}: ${currentQuantity}`}
                    onSelect={handleQuantitySelect}
                >
                    {Array.from({ length: Math.min(10, data.stock) }, (_, i) => i + 1).map((count) => (
                        <Dropdown.Item key={count} eventKey={count.toString()}>
                            {count}
                        </Dropdown.Item>
                    ))}
                </DropdownButton>
            ) : (
                <AddToCartButton product={data} variant={'icon-only'} inCart={inCart} />
            )}
            <AddToFavoritesButton product={data} variant={'icon-only'} />
        </>
    );
}

export default CustomButtonGroup;
