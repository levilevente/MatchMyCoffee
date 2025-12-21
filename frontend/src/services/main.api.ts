import axios from 'axios';

import type { ProductDetail, ProductSummary } from '../types/ProductsType.ts';
import type { ReviewType } from '../types/ReviewType.ts';
import { ALL_PRODUCT_SUMMARY, MOCK_REVIEWS, PRODUCT_DETAIL } from '../utils/DummyProducts.ts';

export const nasaEpicApi = axios.create({
    baseURL: `https://localhost:8080/`,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    },
});

export function getCoffees(): ProductSummary[] {
    return ALL_PRODUCT_SUMMARY;
}

export function getCoffeeById(id: number): ProductDetail {
    return PRODUCT_DETAIL.find((product) => product.id === id)!;
}

export function getProductReviews(productId: number): ReviewType[] {
    return MOCK_REVIEWS.filter((review) => review.productId === productId);
}
