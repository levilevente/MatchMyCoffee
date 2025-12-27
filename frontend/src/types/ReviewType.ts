export interface ReviewType {
    id: number;
    productId: number;
    authorName: string;
    rating: number;
    comment: string | null;
    isApproved: boolean;
    createdAt: string;
}
