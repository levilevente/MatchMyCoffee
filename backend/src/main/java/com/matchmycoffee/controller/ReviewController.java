package com.matchmycoffee.controller;

import com.matchmycoffee.dto.response.review.ReviewResponse;
import com.matchmycoffee.mapper.ReviewMapper;
import com.matchmycoffee.model.entity.Review;
import com.matchmycoffee.service.ReviewService;
import com.matchmycoffee.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/products/{productId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewMapper reviewMapper;

    @GetMapping("")
    public ResponseEntity<Page<ReviewResponse>> getAllProductReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @PathVariable Long productId
    ) throws ServiceException {
        log.info("GET /products/{}/reviews", productId);

        if (size <= 0 || page < 0) {
            log.warn("Invalid pagination parameters: page={}, size={}", page, size);
            return ResponseEntity.badRequest().build();
        }

        if ("id".equals(sortBy) || "rating".equals(sortBy) || "createdAt".equals(sortBy)) {
            log.info("Sorting by {}", sortBy);
        } else {
            log.warn("Invalid sortBy parameter: {}", sortBy);
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Review> reviewPage = reviewService.getAllReviews(productId, pageable);

        return ResponseEntity.ok(reviewPage.map(reviewMapper::toReviewResponse));
    }
}
