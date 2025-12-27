package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.response.product.ProductSummaryResponse;
import com.matchmycoffee.model.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {
    public abstract ProductSummaryResponse toProductSummaryResponse(
            Product product,
            Double averageRating,
            Integer reviewCount
    );
}
