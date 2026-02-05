export const OrderStatus = {
    PENDING: 'PENDING',
    PROCESSING: 'PROCESSING',
    SHIPPED: 'SHIPPED',
    DELIVERED: 'DELIVERED',
    CANCELLED: 'CANCELLED',
} as const;

export type OrderStatus = (typeof OrderStatus)[keyof typeof OrderStatus];

export interface OrderItem {
    itemId?: number;
    productId: number;
    quantity: number;
}

export interface CreateOrderRequest {
    customerEmail: string;
    customerFirstName: string;
    customerLastName: string;
    shippingAddressLine1: string;
    shippingAddressLine2?: string;
    shippingCity: string;
    shippingState: string;
    shippingZip: string;
    shippingCountry: string;
    items: OrderItem[];
}

export interface Order extends CreateOrderRequest {
    id: number;
    status: OrderStatus;
    items: OrderItem[];
}

export interface UpdateOrderRequest {
    customerEmail?: string;
    customerFirstName?: string;
    customerLastName?: string;
    shippingAddressLine1?: string;
    shippingAddressLine2?: string;
    shippingCity?: string;
    shippingState?: string;
    shippingZip?: string;
    shippingCountry?: string;
    status?: OrderStatus;
}
