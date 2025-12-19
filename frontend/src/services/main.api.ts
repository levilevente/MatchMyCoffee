import axios from 'axios';

import type { ProductType } from '../types/ProductsType.ts';
import { PRODUCTS } from '../utils/DummyProducts.ts';

export const nasaEpicApi = axios.create({
    baseURL: `https://localhost:8080/`,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    },
});

export function getCoffees(): ProductType[] {
    return PRODUCTS;
}
