import { Image, Table } from 'react-bootstrap';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router';

import AddToCartButton from '../components/common/AddToCartButton.tsx';
import AddToFavoritesButton from '../components/common/AddToFavoritesButton.tsx';
import StarRating from '../components/common/StarRating.tsx';
import AddReview from '../components/product/AddReview.tsx';
import { useProductById, useProductReviewsByProductId } from '../query/main.query.ts';
import style from './ProductPage.module.css';

function ProductPage() {
    const { productId } = useParams();
    const { data: product, isLoading, error } = useProductById(productId ? parseInt(productId, 10) : 0);
    const { data: reviews, refetchWithInvalidation } = useProductReviewsByProductId(
        productId ? parseInt(productId, 10) : 0,
    );

    const { t } = useTranslation();

    if (isLoading) {
        return <div className={style.container}>Loading...</div>;
    }

    if (error) {
        return <div className={style.container}>Error loading product.</div>;
    }

    if (!product) {
        return <div className={style.container}>Loading...</div>;
    }

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
                        {product.isActive || product.stock <= 0 ? (
                            <div className={style.buttonContainer}>
                                <AddToCartButton product={product} variant={'with-text'} />
                                <AddToFavoritesButton product={product} variant={'with-text'} />
                            </div>
                        ) : (
                            <h2>{t('product.outOfStock')}</h2>
                        )}
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
                                    {Array.isArray(product.origins)
                                        ? product.origins.map((origin, index) => (
                                              <div key={index}>
                                                  {origin.region} ({origin.percentage}%)
                                              </div>
                                          ))
                                        : null}
                                </td>
                            </tr>
                            <tr>
                                <td>{t('product.tastes')}</td>
                                <td className={style.multilineTableCell}>
                                    {Array.isArray(product.tastes)
                                        ? product.tastes.map((taste, index) => (
                                              <div key={index}>
                                                  {taste.name} {taste.category.name}
                                              </div>
                                          ))
                                        : null}
                                </td>
                            </tr>
                            <tr>
                                <td>{t('product.brewingMethods')}</td>
                                <td>
                                    {Array.isArray(product.brewingMethods)
                                        ? product.brewingMethods.map((method, index) => (
                                              <div key={index}>
                                                  {method.name} - {method.description}{' '}
                                                  {method.isOptimal ? '(Optimal)' : '(Not Optimal)'}
                                              </div>
                                          ))
                                        : null}
                                </td>
                            </tr>
                        </tbody>
                    </Table>
                </div>
                {product.isActive || product.stock <= 0 ? (
                    <section id={'reviews'}>
                        <div className={style.reviewsSection}>
                            <h2>{t('product.reviews')}</h2>
                            <AddReview refetchWithInvalidation={refetchWithInvalidation} productId={product.id} />
                            {Array.isArray(reviews) && reviews.length !== 0
                                ? reviews.map((review, index) => (
                                      <div key={`${review.id}-${index}`} className={style.reviewCard}>
                                          <div className={style.reviewHeader}>
                                              <p>{review.authorName}</p>
                                              <StarRating rating={review.rating} size={20} />
                                          </div>
                                          <p>{review.comment}</p>
                                      </div>
                                  ))
                                : null}
                        </div>
                    </section>
                ) : null}
            </div>
        </div>
    );
}

export default ProductPage;
