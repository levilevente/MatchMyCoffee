import { useQuery } from '@tanstack/react-query';

import { getBlogPostById, getBlogPosts, getCoffeeById, getCoffees, getProductReviews } from '../services/main.api.ts';
import { queryClient } from './common.query.ts';

export function useAllProducts(page: number = 0, size: number = 12) {
    const query = useQuery({
        queryKey: ['allProducts', page, size],
        queryFn: () => getCoffees(page, size),
    });

    const refetchWithInvalidation = async () => {
        await queryClient.invalidateQueries({ queryKey: ['allProducts'] });
        return query.refetch();
    };

    return { ...query, refetchWithInvalidation };
}

export function useProductById(id: number) {
    return useQuery({
        queryKey: ['product', id],
        queryFn: () => getCoffeeById(id),
    });
}

export function useProductReviewsByProductId(id: number) {
    const query = useQuery({
        queryKey: ['productReviews', id],
        queryFn: () => getProductReviews(id),
    });

    const refetchWithInvalidation = async () => {
        await queryClient.invalidateQueries({ queryKey: ['productReviews', id] });
        return query.refetch();
    };

    return { ...query, refetchWithInvalidation };
}

export function useBlogPosts() {
    return useQuery({
        queryKey: ['blogPosts'],
        queryFn: () => getBlogPosts(),
    });
}

export function useBlogPostById(id: number) {
    return useQuery({
        queryKey: ['blogPost', id],
        queryFn: () => getBlogPostById(id),
    });
}
