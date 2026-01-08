package com.matchmycoffee.service.impl;

import com.matchmycoffee.mapper.ReviewMapper;
import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.model.entity.Review;
import com.matchmycoffee.repository.ProductRepository;
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

import java.util.Objects;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewMapper reviewMapper;

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
    public Review createReview(
            Long productId,
            Review review
    ) throws IllegalReviewArgumentException, ServiceException {
        log.info("Creating review for product id: {}", productId);
        validateRating(review.getRating());

        try {
            Product product = findProductById(productId);
            review.setIsApproved(false);
            review.setProduct(product);
            return reviewRepository.save(review);
        } catch (EntityNotFoundException e) {
            log.error("Product with id {} not found", productId, e);
            throw new IllegalReviewArgumentException("Product not found", e);
        } catch (IllegalArgumentException e) {
            log.error("Error while creating review for product id {}", productId, e);
            throw new ServiceException("Failed to create review", e);
        }
    }

    @Override
    public Review updateReview(Long productId, Long id, Review reviewDetails) throws ReviewNotFoundException {
        log.info("Updating review with id: {}", id);
        try {

            Review existingReview = reviewRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Review with id {} not found", id);
                        return new ReviewNotFoundException("Review not found!");
                    });

            if (!Objects.equals(existingReview.getProduct().getId(), productId)) {
                log.error("Review with id {} does not belong to the specified product", productId);
                throw new ReviewNotFoundException("Review does not belong to the specified product!");
            }

            Review mergedReview = reviewMapper.updateEntity(existingReview, reviewDetails);
            mergedReview.setId(existingReview.getId());

            log.info("Review updated successfully: {}", mergedReview);

            return reviewRepository.save(mergedReview);
        } catch (EntityNotFoundException e) {
            log.error("Error while updating review with id {}", id, e);
            throw new ReviewNotFoundException("Review not found!", e);
        }
    }

    @Override
    public void deleteReview(Long id) throws ServiceException {

    }

    private void validateRating(int rating) throws IllegalReviewArgumentException {
        if (rating < 1 || rating > 5) {
            log.error("Invalid rating value: {}", rating);
            throw new IllegalReviewArgumentException("Rating must be between 1 and 5");
        }
    }

    private Product findProductById(Long productId) throws IllegalReviewArgumentException {
        return productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", productId);
                    return new IllegalReviewArgumentException("Product not found");
                });
    }
}
