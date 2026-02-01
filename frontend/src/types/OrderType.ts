export interface OrderItem {
    productId: number;
    quantity: number;
}

export interface Order {
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