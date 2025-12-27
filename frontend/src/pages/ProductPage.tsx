import { useState } from 'react';
import { Image, Table } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router';

import AddToCartButton from '../components/common/AddToCartButton.tsx';
import AddToFavoritesButton from '../components/common/AddToFavoritesButton.tsx';
import StarRating from '../components/common/StarRating.tsx';
import AddReview from '../components/product/AddReview.tsx';
import { getCoffeeById, getProductReviews } from '../services/main.api.ts';
import style from './ProductPage.module.css';

function ProductPage() {
    const { productId } = useParams();
    const product = getCoffeeById(productId ? parseInt(productId) : 0);
    const [reviews, setReviews] = useState(getProductReviews(productId ? parseInt(productId) : 0));

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
                            <a href={'#reviews'} className={style.reviewLink}>
                                {product.averageRating} ({product.reviewCount} reviews)
                            </a>
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
            <div className={style.detailsSection}>
                <div className={style.description}>
                    <h2>{t('product.description')}</h2>
                    <p>{product.description}</p>
                </div>
                <div className={style.specificationsSection}>
                    <h2>{t('product.specification')}</h2>
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
                <section id={'reviews'}>
                    <div className={style.reviewsSection}>
                        <h2>{t('product.reviews')}</h2>
                        <AddReview setReviews={setReviews} productId={product.id} />
                        {reviews.length !== 0 ? (
                            reviews.map((review) => (
                                <div key={review.id} className={style.reviewCard}>
                                    <div className={style.reviewHeader}>
                                        <p>{review.authorName}</p>
                                        <StarRating rating={review.rating} size={20} />
                                    </div>
                                    <p>{review.comment}</p>
                                </div>
                            ))
                        ) : (
                            <p>No reviews available.</p>
                        )}
                    </div>
                </section>
            </div>
        </div>
    );
}

export default ProductPage;
