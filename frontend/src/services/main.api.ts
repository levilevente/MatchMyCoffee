import axios from 'axios';

import type { BlogPostDetailedType, BlogPostType } from '../types/BlogPostType.ts';
import type { ProductDetail, ProductSummary } from '../types/ProductsType.ts';
import type { ReviewType } from '../types/ReviewType.ts';
import {
    ALL_BLOG_POSTS_SUMMARY,
    ALL_PRODUCT_SUMMARY,
    BLOG_POSTS,
    MOCK_REVIEWS,
    PRODUCT_DETAIL,
} from '../utils/DummyProducts.ts';

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

export function getBlogPosts(): BlogPostType[] {
    return ALL_BLOG_POSTS_SUMMARY;
}

export function getBlogPostById(id: number): BlogPostDetailedType {
    return BLOG_POSTS.find((blogPost) => blogPost.id === id)!;
}
