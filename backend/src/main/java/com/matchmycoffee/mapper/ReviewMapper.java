package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.request.review.ReviewRequest;
import com.matchmycoffee.dto.request.review.ReviewUpdateRequest;
import com.matchmycoffee.dto.response.review.ReviewResponse;
import com.matchmycoffee.model.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "id", ignore = true)
    Review toEntity(ReviewRequest reviewResponse);

    @Mapping(target = "id", ignore = true)
    Review toEntity(ReviewUpdateRequest reviewResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "id", ignore = true)
    Review updateEntity(@MappingTarget Review review, Review updatedReview);
}
