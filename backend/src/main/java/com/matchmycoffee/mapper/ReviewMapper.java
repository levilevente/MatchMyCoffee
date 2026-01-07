package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.response.review.ReviewResponse;
import com.matchmycoffee.model.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {


    ReviewResponse toReviewResponse(Review review);
}
