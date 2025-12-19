import React from 'react';
import styles from './StarRating.module.css';

interface StarRatingProps {
    rating: number;
    maxStars?: number;
    size?: number;
    showCount?: number;
}

const StarRating: React.FC<StarRatingProps> = ({
                                                   rating,
                                                   maxStars = 5,
                                                   size = 20,
                                                   showCount
                                               }) => {
    const renderStars = () => {
        const stars = [];
        for (let i = 1; i <= maxStars; i++) {
            if (rating >= i) {
                // Full Star
                stars.push(<FullStar key={i} size={size}/>);
            } else if (rating >= i - 0.5) {
                // Half Star
                stars.push(<HalfStar key={i} size={size}/>);
            } else {
                // Empty Star
                stars.push(<EmptyStar key={i} size={size}/>);
            }
        }
        return stars;
    };

    return (
        <div className={styles.container}>
            <div className={styles.stars}>
                {renderStars()}
            </div>
            {showCount !== undefined && (
                <span className={styles.count}>({showCount})</span>
            )}
        </div>
    );
};

const FullStar = ({size}: { size: number }) => (
    <svg width={size} height={size} viewBox="0 0 24 24" className={styles.starFilled}>
        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
    </svg>
);

const HalfStar = ({size}: { size: number }) => (
    <svg width={size} height={size} viewBox="0 0 24 24" className={styles.starHalf}>
        <defs>
            <linearGradient id="halfGrad">
                <stop offset="50%" stopColor="currentColor" className={styles.stopFilled}/>
                <stop offset="50%" stopColor="transparent" className={styles.stopEmpty}/>
            </linearGradient>
        </defs>
        <path fill="url(#halfGrad)" stroke="currentColor" strokeWidth="0" d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
        {/* Outline for the empty half */}
        <path fill="none" stroke="currentColor" strokeWidth="0" className={styles.starOutline} d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
    </svg>
);

const EmptyStar = ({size}: { size: number }) => (
    <svg width={size} height={size} viewBox="0 0 24 24" className={styles.starEmpty}>
        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
    </svg>
);

export default StarRating;