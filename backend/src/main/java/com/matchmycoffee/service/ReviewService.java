package com.matchmycoffee.service;

import com.matchmycoffee.model.entity.Review;
import com.matchmycoffee.service.exception.IllegalReviewArgumentException;
import com.matchmycoffee.service.exception.ReviewNotFoundException;
import com.matchmycoffee.service.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Review getReviewById(Long id) throws ReviewNotFoundException;

    Page<Review> getAllReviews(Long productId, Pageable pageable) throws ServiceException;

    Review createReview(Review review) throws IllegalReviewArgumentException, ServiceException;

    Review updateReview(Long id, Review reviewDetails) throws ReviewNotFoundException;

    void deleteReview(Long id) throws ServiceException;
}
