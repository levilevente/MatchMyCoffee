import axios from 'axios';

import type { ProductDetail, ProductSummary } from '../types/ProductsType.ts';
import { ALL_PRODUCT_SUMMARY, PRODUCT_DETAIL } from '../utils/DummyProducts.ts';

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
