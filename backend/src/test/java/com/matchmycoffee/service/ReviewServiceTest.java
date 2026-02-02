package com.matchmycoffee.service;

import com.matchmycoffee.mapper.ReviewMapper;
import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.model.entity.Review;
import com.matchmycoffee.repository.ProductRepository;
import com.matchmycoffee.repository.ReviewRepository;
import com.matchmycoffee.service.exception.IllegalReviewArgumentException;
import com.matchmycoffee.service.exception.ReviewNotFoundException;
import com.matchmycoffee.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@SuppressWarnings("PMD")
class ReviewServiceTest {

    private Review review1;
    private Review review2;
    private Review review3;
    private Product product1;
    private Product product2;

    @Autowired
    private ReviewService reviewService;

    @MockitoBean
    private ReviewRepository reviewRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private ReviewMapper reviewMapper;

    @BeforeEach
    public void setup() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Ethiopian Yirgacheffe");
        product1.setDescription("Bright and fruity coffee");
        product1.setPrice(15.99);
        product1.setStock(100);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Colombian Supremo");
        product2.setDescription("Smooth and balanced");
        product2.setPrice(12.99);
        product2.setStock(50);

        review1 = new Review();
        review1.setId(1L);
        review1.setProduct(product1);
        review1.setAuthorName("John Doe");
        review1.setRating(5);
        review1.setComment("Excellent coffee!");
        review1.setIsApproved(true);

        review2 = new Review();
        review2.setId(2L);
        review2.setProduct(product1);
        review2.setAuthorName("Jane Smith");
        review2.setRating(4);
        review2.setComment("Very good, but a bit pricey");
        review2.setIsApproved(false);

        review3 = new Review();
        review3.setId(3L);
        review3.setProduct(product2);
        review3.setAuthorName("Bob Wilson");
        review3.setRating(3);
        review3.setComment("Decent coffee");
        review3.setIsApproved(true);
    }

    @Test
    public void testGetAllReviews_Success() throws ServiceException {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Review> reviews = List.of(review1, review2);
        Page<Review> reviewPage = new PageImpl<>(reviews, pageable, reviews.size());

        given(reviewRepository.findAllByProductId(1L, pageable)).willReturn(reviewPage);

        // When
        Page<Review> result = reviewService.getAllReviews(1L, pageable);

        // Then
        assertNotNull(result, "Resulting page should not be null");
        assertEquals(2, result.getTotalElements(), "Total elements should be 2");
        assertEquals(2, result.getContent().size(), "Content size should be 2");
        assertEquals("John Doe", result.getContent().get(0).getAuthorName(), "First review author name should match");
        assertEquals(
                "Jane Smith",
                result.getContent().get(1).getAuthorName(),
                "Second review author name should match"
        );
    }

    @Test
    public void testGetReviewById_Success() throws ReviewNotFoundException {
        // Given
        given(reviewRepository.findById(1L)).willReturn(Optional.of(review1));

        // When
        Review result = reviewService.getReviewById(1L);

        // Then
        assertNotNull(result, "Returned review should not be null");
        assertEquals(1L, result.getId(), "Review ID should match");
        assertEquals("John Doe", result.getAuthorName(), "Author name should match");
        assertEquals(5, result.getRating(), "Rating should match");
        assertEquals("Excellent coffee!", result.getComment(), "Comment should match");
        assertTrue(result.getIsApproved(), "Review should be approved");
    }

    @Test
    public void testGetReviewById_NotFound() {
        // Given
        given(reviewRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(ReviewNotFoundException.class, () ->
            reviewService.getReviewById(999L),
            "Review not found!"
        );
    }

    @Test
    public void testCreateReview_Success() throws IllegalReviewArgumentException, ServiceException {
        // Given
        Review newReview = new Review();
        newReview.setAuthorName("Alice Brown");
        newReview.setRating(5);
        newReview.setComment("Amazing taste!");

        given(productRepository.findById(1L)).willReturn(Optional.of(product1));
        given(reviewRepository.save(any(Review.class))).willReturn(newReview);

        // When
        Review result = reviewService.createReview(1L, newReview);

        // Then
        assertNotNull(result, "Created review should not be null");
        assertEquals("Alice Brown", result.getAuthorName(), "Author name should match");
        assertEquals(5, result.getRating(), "Rating should match");
        assertEquals("Amazing taste!", result.getComment(), "Comment should match");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void testCreateReview_InvalidRating_TooLow() {
        // Given
        Review newReview = new Review();
        newReview.setAuthorName("Alice Brown");
        newReview.setRating(0);
        newReview.setComment("Invalid rating");

        // When & Then
        assertThrows(IllegalReviewArgumentException.class, () ->
            reviewService.createReview(1L, newReview),
            "Rating must be between 1 and 5"
        );
    }

    @Test
    public void testCreateReview_InvalidRating_TooHigh() {
        // Given
        Review newReview = new Review();
        newReview.setAuthorName("Alice Brown");
        newReview.setRating(6);
        newReview.setComment("Invalid rating");

        // When & Then
        assertThrows(IllegalReviewArgumentException.class, () ->
            reviewService.createReview(1L, newReview),
            "Rating must be between 1 and 5"
        );
    }

    @Test
    public void testCreateReview_ProductNotFound() {
        // Given
        Review newReview = new Review();
        newReview.setAuthorName("Alice Brown");
        newReview.setRating(5);
        newReview.setComment("Great coffee!");

        given(productRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalReviewArgumentException.class, () ->
            reviewService.createReview(999L, newReview),
            "Product not found"
        );
    }

    @Test
    public void testUpdateReview_Success() throws ReviewNotFoundException {
        // Given
        Review updatedReview = new Review();
        updatedReview.setAuthorName("John Doe Updated");
        updatedReview.setRating(4);
        updatedReview.setComment("Updated comment");

        Review mergedReview = new Review();
        mergedReview.setId(1L);
        mergedReview.setProduct(product1);
        mergedReview.setAuthorName("John Doe Updated");
        mergedReview.setRating(4);
        mergedReview.setComment("Updated comment");

        given(reviewRepository.findById(1L)).willReturn(Optional.of(review1));
        given(reviewMapper.updateEntity(review1, updatedReview)).willReturn(mergedReview);
        given(reviewRepository.save(any(Review.class))).willReturn(mergedReview);

        // When
        Review result = reviewService.updateReview(1L, 1L, updatedReview);

        // Then
        assertNotNull(result, "Updated review should not be null");
        assertEquals(1L, result.getId(), "Review ID should match");
        assertEquals("John Doe Updated", result.getAuthorName(), "Author name should be updated");
        assertEquals(4, result.getRating(), "Rating should be updated");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void testUpdateReview_NotFound() {
        // Given
        Review updatedReview = new Review();
        updatedReview.setAuthorName("Updated Name");
        updatedReview.setRating(4);

        given(reviewRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(ReviewNotFoundException.class, () ->
            reviewService.updateReview(1L, 999L, updatedReview),
            "Review not found!"
        );
    }

    @Test
    public void testUpdateReview_WrongProduct() {
        // Given
        Review updatedReview = new Review();
        updatedReview.setAuthorName("Updated Name");
        updatedReview.setRating(4);

        given(reviewRepository.findById(3L)).willReturn(Optional.of(review3));

        // When & Then - review3 belongs to product2, but we're trying to update with product1
        assertThrows(ReviewNotFoundException.class, () ->
            reviewService.updateReview(1L, 3L, updatedReview),
            "Review does not belong to the specified product!"
        );
    }

    @Test
    public void testDeleteReview_Success() throws ServiceException {
        // Given
        doNothing().when(reviewRepository).deleteById(1L);

        // When
        reviewService.deleteReview(1L);

        // Then
        verify(reviewRepository, times(1)).deleteById(1L);
    }
}

