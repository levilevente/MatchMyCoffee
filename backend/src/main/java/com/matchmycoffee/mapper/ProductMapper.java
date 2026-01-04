package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.response.BrewingMethodResponse;
import com.matchmycoffee.dto.response.OriginResponse;
import com.matchmycoffee.dto.response.TasteCategoryResponse;
import com.matchmycoffee.dto.response.TasteResponse;
import com.matchmycoffee.dto.response.product.ProductDetailResponse;
import com.matchmycoffee.dto.response.product.ProductSpecifications;
import com.matchmycoffee.dto.response.product.ProductSummaryResponse;
import com.matchmycoffee.model.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "calculatedAverageRating", target = "averageRating")
    @Mapping(source = "calculatedReviewCount", target = "reviewCount")
    ProductSummaryResponse toProductSummaryResponse(Product product);

    @Mapping(source = "calculatedAverageRating", target = "averageRating")
    @Mapping(source = "calculatedReviewCount", target = "reviewCount")
    @Mapping(source = ".", target = "specifications")
    @Mapping(source = "productOrigins", target = "origins")
    @Mapping(source = "productBrewingMethods", target = "brewingMethods")
    @Mapping(source = "productTastes", target = "tastes")
    ProductDetailResponse toProductDetailResponse(Product product);

    @Mapping(source = "roastLevel", target = "roastLevel")
    @Mapping(source = "acidityScore", target = "acidityScore")
    ProductSpecifications toSpecifications(Product product);

    @Mapping(source = "origin.id", target = "id")
    @Mapping(source = "origin.region", target = "region")
    @Mapping(source = "origin.continent", target = "continent")
    @Mapping(source = "percentage", target = "percentage")
    OriginResponse toOriginResponse(ProductOrigin productOrigin);

    @Mapping(source = "brewingMethod.id", target = "id")
    @Mapping(source = "brewingMethod.name", target = "name")
    @Mapping(source = "brewingMethod.description", target = "description")
    @Mapping(source = "brewingMethod.iconUrl", target = "iconUrl")
    @Mapping(source = "isOptimal", target = "isOptimal")
    BrewingMethodResponse toBrewingMethodResponse(ProductBrewingMethod productBrewingMethod);

    @Mapping(source = "taste.id", target = "id")
    @Mapping(source = "taste.name", target = "name")
    @Mapping(source = "taste.category", target = "category")
    TasteResponse toTasteResponse(ProductTaste productTaste);

    TasteCategoryResponse toTasteCategoryResponse(TasteCategory category);
}
