export interface ProductSummary {
    // Basic information about a coffee product

    id: number;
    name: string;
    price: number;
    imageUrl: string;
    isBlend: boolean;

    specifications: {
        roastLevel: number;
    };

    averageRating: number;
    reviewCount: number;
}

export interface ProductDetail extends ProductSummary {
    description: string;
    stock: number;
    isActive: boolean;

    specifications: {
        roastLevel: number;
        acidityScore: number;
    };

    origins: ProductOrigin[];

    tastes: ProductTaste[];

    brewingMethods: ProductBrewingMethod[];
}

export interface ProductOrigin {
    // Information about the origin of the coffee beans
    id: number;
    region: string;
    continent: string;

    percentage: number;
}

export interface ProductBrewingMethod {
    // Information about a brewing method for the coffee
    id: number;
    name: string;
    description: string;
    iconUrl: string | null;

    isOptimal: boolean;
}

export interface ProductTaste {
    name: string;
    category: {
        name: string;
        colorCode: string;
    };
}
