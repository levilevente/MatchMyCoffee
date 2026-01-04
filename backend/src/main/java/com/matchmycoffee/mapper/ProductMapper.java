package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.response.product.ProductSummaryResponse;
import com.matchmycoffee.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "calculatedAverageRating", target = "averageRating")
    @Mapping(source = "calculatedReviewCount", target = "reviewCount")
    ProductSummaryResponse toProductSummaryResponse(Product product);
}
