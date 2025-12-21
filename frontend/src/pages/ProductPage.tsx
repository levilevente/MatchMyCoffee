import { Image, Table } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router';

import AddToCartButton from '../components/common/AddToCartButton.tsx';
import AddToFavoritesButton from '../components/common/AddToFavoritesButton.tsx';
import StarRating from '../components/common/StarRating.tsx';
import { getCoffeeById } from '../services/main.api.ts';
import style from './ProductPage.module.css';

function ProductPage() {
    const { productId } = useParams();
    const product = getCoffeeById(productId ? parseInt(productId) : 0);

    const { t } = useTranslation();

    return (
        <div className={style.container}>
            <div className={style.mainSection}>
                <div className={style.leftSection}>
                    <Image src={product?.imageUrl} alt="product" className={style.image} />
                </div>
                <div className={style.rightSection}>
                    <div>
                        <div>
                            <h3>{product?.origins[0].region}</h3>
                            <h1>{product.name}</h1>
                        </div>
                        <div className={style.ratingContainer}>
                            <StarRating rating={product.averageRating} size={30} />
                            <h4>
                                {product.averageRating} ({product.reviewCount} reviews)
                            </h4>
                        </div>
                    </div>
                    <div className={style.purchaseSection}>
                        <h3>{product.price} $</h3>
                        <div className={style.buttonContainer}>
                            <AddToCartButton product={product} variant={'with-text'} />
                            <AddToFavoritesButton product={product} variant={'with-text'} />
                        </div>
                    </div>
                </div>
            </div>
            <div className={style.specificationsSection}>
                <div className={style.description}>
                    <h3>Description</h3>
                    <p>{product.description}</p>
                </div>
                <div>
                    <h3>Specifications</h3>
                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>{t('product.specifications')}</th>
                                <th>{t('product.value')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{t('product.roastLevel')}</td>
                                <td>{product.specifications.roastLevel}</td>
                            </tr>
                            <tr>
                                <td>{t('product.acidity')}</td>
                                <td>{product.specifications.acidityScore}</td>
                            </tr>
                            <tr>
                                <td>{t('product.origin')}</td>
                                <td className={style.multilineTableCell}>
                                    {product.origins.map((origin) => `${origin.region} (${origin.percentage}%)\n`)}
                                </td>
                            </tr>
                            <tr>
                                <td>{t('product.tastes')}</td>
                                <td className={style.multilineTableCell}>
                                    {product.tastes.map((taste) => `${taste.name} ${taste.category.name}\n`)}
                                </td>
                            </tr>
                            <tr>
                                <td>{t('product.brewingMethods')}</td>
                                <td>
                                    {product.brewingMethods.map(
                                        (method) =>
                                            `${method.name} - ${method.description} ${method.isOptimal ? '(Optimal)' : '(Not Optimal)'}\n`,
                                    )}
                                </td>
                            </tr>
                        </tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
}

export default ProductPage;
