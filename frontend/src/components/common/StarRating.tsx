import React from 'react';

import styles from './StarRating.module.css';

interface StarRatingProps {
    rating: number;
    maxStars?: number;
    size?: number;
    showCount?: number;
    clickable?: boolean;
    onRatingChange?: (newRating: number) => void;
}

const StarRating: React.FC<StarRatingProps> = ({
    rating,
    maxStars = 5,
    size = 20,
    showCount,
    clickable = false,
    onRatingChange,
}) => {
    const handleStarClick = (index: number) => {
        if (clickable && onRatingChange) {
            onRatingChange(index);
        }
    };

    const renderStars = () => {
        const stars = [];
        for (let i = 1; i <= maxStars; i++) {
            const starElement = {
                key: i,
                size: size,
                onClick: () => handleStarClick(i),
                style: { cursor: clickable ? 'pointer' : 'default' },
            };

            if (rating >= i) {
                // Full Star
                stars.push(<FullStar {...starElement} />);
            } else if (rating >= i - 0.5) {
                // Half Star
                stars.push(<HalfStar {...starElement} />);
            } else {
                // Empty Star
                stars.push(<EmptyStar {...starElement} />);
            }
        }
        return stars;
    };

    return (
        <div className={styles.container}>
            <div className={styles.stars}>{renderStars()}</div>
            {showCount !== undefined ? <span className={styles.count}>({showCount})</span> : null}
        </div>
    );
};

interface StarIconProps extends React.SVGProps<SVGSVGElement> {
    size: number;
}

const FullStar = ({ size, ...props }: StarIconProps) => (
    <svg width={size} height={size} viewBox="0 0 24 24" className={styles.starFilled} {...props}>
        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
    </svg>
);

const HalfStar = ({ size, ...props }: StarIconProps) => (
    <svg width={size} height={size} viewBox="0 0 24 24" className={styles.starHalf} {...props}>
        <defs>
            <linearGradient id="halfGrad">
                <stop offset="50%" stopColor="currentColor" className={styles.stopFilled} />
                <stop offset="50%" stopColor="transparent" className={styles.stopEmpty} />
            </linearGradient>
        </defs>
        <path
            fill="url(#halfGrad)"
            stroke="currentColor"
            strokeWidth="0"
            d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"
        />
        <path
            fill="none"
            stroke="currentColor"
            strokeWidth="0"
            className={styles.starOutline}
            d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"
        />
    </svg>
);

const EmptyStar = ({ size, ...props }: StarIconProps) => (
    <svg width={size} height={size} viewBox="0 0 24 24" className={styles.starEmpty} {...props}>
        <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
    </svg>
);

export default StarRating;
