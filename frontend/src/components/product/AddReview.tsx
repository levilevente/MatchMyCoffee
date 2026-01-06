import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';

import type { ReviewType } from '../../types/ReviewType.ts';
import MainButton from '../common/MainButton.tsx';
import StarRating from '../common/StarRating.tsx';
import style from './AddReview.module.css';

interface AddReviewsProps {
    setReviews: React.Dispatch<React.SetStateAction<ReviewType[]>>;
    productId: number;
}

function AddReview(props: AddReviewsProps) {
    const { setReviews, productId } = props;

    const { t } = useTranslation();
    const [stars, setStars] = useState<number>(0);
    const [reviewText, setReviewText] = useState<string>('');
    const [reviewerName, setReviewerName] = useState<string>('');

    return (
        <div className={style.reviewCard}>
            <h2>Add a Review</h2>
            <div className={style.starRating}>
                <StarRating rating={stars} maxStars={5} size={30} onRatingChange={setStars} clickable />
            </div>
            <textarea
                className={style.textArea}
                placeholder={t('product.reviewerNamePlaceholder')}
                value={reviewerName}
                onChange={(e) => setReviewerName(e.target.value)}
            />
            <textarea
                className={style.textArea}
                placeholder={t('product.writeReviewPlaceholder')}
                value={reviewText}
                onChange={(e) => setReviewText(e.target.value)}
            />
            <MainButton
                text={'Submit Review'}
                onClick={() => {
                    const newReview: ReviewType = {
                        createdAt: '',
                        id: Date.now(),
                        productId: productId,
                        rating: stars,
                        comment: reviewText,
                        authorName: reviewerName ? reviewerName : 'Anonymous',
                        isApproved: true,
                    };
                    setReviews((prevReviews) => [newReview, ...prevReviews]);
                    setStars(0);
                    setReviewText('');
                }}
            />
        </div>
    );
}

export default AddReview;
