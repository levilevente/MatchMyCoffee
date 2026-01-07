package com.matchmycoffee.service.impl;

import com.matchmycoffee.model.entity.Review;
import com.matchmycoffee.repository.ReviewRepository;
import com.matchmycoffee.service.ReviewService;
import com.matchmycoffee.service.exception.IllegalReviewArgumentException;
import com.matchmycoffee.service.exception.ReviewNotFoundException;
import com.matchmycoffee.service.exception.ServiceException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review getReviewById(Long id) throws ReviewNotFoundException {
        log.info("Getting review by id: {}", id);
        try {
            return reviewRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Review with id {} not found", id);
                        return new ReviewNotFoundException("Review not found!");
                    });
        } catch (EntityNotFoundException e) {
            log.error("Error while retrieving review with id {}", id, e);
            throw new ReviewNotFoundException("Review not found!", e);
        }
    }

    @Override
    public Page<Review> getAllReviews(Long productId, Pageable pageable) throws ServiceException {
        log.info("Getting all reviews for product id: {}", productId);
        try {
            return reviewRepository.findAllByProductId(productId, pageable);
        } catch (EntityNotFoundException e) {
            log.error("Error while retrieving reviews for product id {}", productId, e);
            throw new ServiceException("Failed to retrieve reviews", e);
        }
    }

    @Override
    public Review createReview(Review review) throws IllegalReviewArgumentException, ServiceException {
        return null;
    }

    @Override
    public Review updateReview(Long id, Review reviewDetails) throws ReviewNotFoundException {
        return null;
    }

    @Override
    public void deleteReview(Long id) throws ServiceException {

    }
}
