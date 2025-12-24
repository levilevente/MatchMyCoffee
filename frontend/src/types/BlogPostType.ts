export interface BlogPostDetailedType {
    id: number;
    title: string;
    content: string;
    authorRole: string;
    publishedAt: string;
    isPublished: boolean;
}

export interface BlogPostType {
    id: number;
    title: string;
    publishedAt: string;
    isPublished: boolean;
}
