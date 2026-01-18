import { useState } from 'react';
import { useTranslation } from 'react-i18next';

import { submitReview } from '../../services/main.api.ts';
import MainButton from '../common/MainButton.tsx';
import StarRating from '../common/StarRating.tsx';
import style from './AddReview.module.css';

interface AddReviewsProps {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    refetchWithInvalidation: () => any;
    productId: number;
}

function AddReview(props: AddReviewsProps) {
    const { refetchWithInvalidation, productId } = props;

    const { t } = useTranslation();
    const [stars, setStars] = useState<number>(0);
    const [reviewText, setReviewText] = useState<string>('');
    const [reviewerName, setReviewerName] = useState<string>('');
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

    const handleSubmit = async () => {
        if (stars === 0) {
            alert('Please provide a rating');
            return;
        }

        setIsSubmitting(true);
        try {
            await submitReview(productId, {
                authorName: reviewerName || 'Anonymous',
                rating: stars,
                comment: reviewText,
            });
            await refetchWithInvalidation();
            setStars(0);
            setReviewText('');
            setReviewerName('');
        } catch (error) {
            console.error('Error submitting review:', error);
            alert('Failed to submit review. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

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
            <MainButton text={isSubmitting ? 'Submitting...' : 'Submit Review'} onClick={() => void handleSubmit()} />
        </div>
    );
}

export default AddReview;
