import axios from 'axios';

import type { BlogPostDetailedType, BlogPostType } from '../types/BlogPostType.ts';
import type { Order } from '../types/OrderType.ts';
import type { ProductDetail, ProductSummary } from '../types/ProductsType.ts';
import type { ReviewType } from '../types/ReviewType.ts';

export const mainAPI = axios.create({
    baseURL: `http://localhost:8080/`,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    },
});

export async function getCoffees(
    page = 0,
    size = 12,
): Promise<{ content: ProductSummary[]; totalPages: number; totalElements: number }> {
    const response = await mainAPI.get<{ content: ProductSummary[]; totalPages: number; totalElements: number }>(
        '/products',
        {
            params: {
                page,
                size,
            },
        },
    );
    return response.data;
}

export async function getCoffeeById(id: number): Promise<ProductDetail> {
    const response = await mainAPI.get<ProductDetail>(`/products/${id}`);
    return response.data;
}

export async function getProductReviews(productId: number): Promise<ReviewType[]> {
    const response = await mainAPI.get<{ content: ReviewType[] }>(`/products/${productId}/reviews?page=0&size=100`);
    return response.data.content;
}

export async function submitReview(
    productId: number,
    review: { authorName: string; rating: number; comment: string },
): Promise<ReviewType> {
    const response = await mainAPI.post<ReviewType>(`/products/${productId}/reviews`, review);
    return response.data;
}

export async function getBlogPosts(): Promise<BlogPostType[]> {
    const response = await mainAPI.get<{ content: BlogPostType[] }>('/blog-posts');
    return response.data.content;
}

export async function getBlogPostById(id: number): Promise<BlogPostDetailedType> {
    const response = await mainAPI.get<BlogPostDetailedType>(`/blog-posts/${id}`);
    return response.data;
}

export async function getOrderByEmail(email: string): Promise<Order[]> {
    const response = await mainAPI.get<{ content: Order[] }>(`/orders`, {
        params: {
            email,
        },
    });
    return response.data.content;
}

export async function createOrder(order: Order): Promise<Order> {
    const response = await mainAPI.post<Order>('/orders', order);
    return response.data;
}

export async function editOrder(orderId: number, order: Partial<Order>): Promise<Order> {
    const response = await mainAPI.patch<Order>(`/orders/${orderId}`, order);
    return response.data;
}

export async function submitOrder(orderId: number, order: Partial<Order>): Promise<Order> {
    const response = await mainAPI.put<Order>(`/orders/${orderId}/submit`, order);
    return response.data;
}