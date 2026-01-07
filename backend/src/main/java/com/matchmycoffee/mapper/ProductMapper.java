package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.request.product.ProductBrewingMethodRequest;
import com.matchmycoffee.dto.request.product.ProductCreatedRequest;
import com.matchmycoffee.dto.request.product.ProductOriginRequest;
import com.matchmycoffee.dto.response.BrewingMethodResponse;
import com.matchmycoffee.dto.response.OriginResponse;
import com.matchmycoffee.dto.response.TasteCategoryResponse;
import com.matchmycoffee.dto.response.TasteResponse;
import com.matchmycoffee.dto.response.product.ProductDetailResponse;
import com.matchmycoffee.dto.response.product.ProductSpecifications;
import com.matchmycoffee.dto.response.product.ProductSummaryResponse;
import com.matchmycoffee.model.entity.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    // To DTO mappings
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


    // To Entity mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productOrigins", source = "origins")
    @Mapping(target = "productBrewingMethods", source = "brewingMethods")
    @Mapping(target = "productTastes", source = "tastes")
    Product toEntity(ProductCreatedRequest productSummaryResponse);

    @Mapping(target = "id.originId", source = "id")
    @Mapping(target = "origin.id", source = "id")
    @Mapping(target = "percentage", source = "percentage")
    @Mapping(target = "product", ignore = true)
    ProductOrigin toProductOrigin(ProductOriginRequest request);

    @Mapping(target = "id.brewingMethodId", source = "id")
    @Mapping(target = "brewingMethod.id", source = "id")
    @Mapping(target = "isOptimal", source = "isOptimal")
    @Mapping(target = "product", ignore = true)
    ProductBrewingMethod toProductBrewingMethod(ProductBrewingMethodRequest request);

    @Mapping(target = "id.tasteId", source = "tasteId")
    @Mapping(target = "taste.id", source = "tasteId")
    @Mapping(target = "product", ignore = true)
    ProductTaste toProductTaste(Long tasteId);

    /**
     *  This runs after the mapping is done to ensure the children know who their parent is.
     */
    @AfterMapping
    default void linkBiDirectionalReferences(@MappingTarget Product product) {
        if (product.getProductOrigins() != null) {
            product.getProductOrigins().forEach(po -> po.setProduct(product));
        }
        if (product.getProductBrewingMethods() != null) {
            product.getProductBrewingMethods().forEach(pbm -> pbm.setProduct(product));
        }
        if (product.getProductTastes() != null) {
            product.getProductTastes().forEach(pt -> pt.setProduct(product));
        }
    }
}
