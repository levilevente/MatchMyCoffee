export interface ProductType {
    name: string;
    id: number;
    description: string;
    price: number;
    stock: number;
    is_active?: boolean;
    image_url?: string;
    is_blend?: boolean;
    roast_level: number; // 1-5
    acidity_score?: number; // 1-5
    created_at?: string;
    updated_at?: string;
}
