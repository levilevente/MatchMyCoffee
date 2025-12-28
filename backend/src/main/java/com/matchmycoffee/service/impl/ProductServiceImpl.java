package com.matchmycoffee.service.impl;

import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.model.entity.Review;
import com.matchmycoffee.repository.ProductRepository;
import com.matchmycoffee.repository.ReviewRepository;
import com.matchmycoffee.service.ProductService;
import com.matchmycoffee.service.exception.BusinessException;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
import com.matchmycoffee.service.exception.ServiceException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Product getProductById(Long id) throws ProductNotAvailableException {
        log.info("Getting product by id: {}", id);
        try {
            return productRepository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            log.error("Product with id {} not found", id, e);
            throw new ProductNotAvailableException("Product not found!", e);
        }
    }

    @Override
    public Optional<Product> findProductByName(String name) throws BusinessException {
        log.info("Finding product by name: {}", name);
        return Optional.ofNullable(productRepository.findByName(name));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) throws BusinessException {
        log.info(
                "Fetching all products with pagination: page number {}, page size {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        // TODO: Add validation logic here
        log.info("Creating new product: {}", product.getName());
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product productDetails) throws ProductNotAvailableException {
        try {
            if (!productRepository.existsById(id)) {
                log.error("Product with id {} not found for update", id);
                throw new ProductNotAvailableException("Product not found!");
            }

            Optional<Product> product = productRepository.findById(id);

            if (product.isEmpty()) {
                log.error("Product with id {} not found for update", id);
                throw new ProductNotAvailableException("Product not found!");
            }
            Product productEntity = product.get();
            productEntity.setName(productDetails.getName());
            productEntity.setDescription(productDetails.getDescription());
            productEntity.setPrice(productDetails.getPrice());
            productEntity.setStock(productDetails.getStock());
            productEntity.setIsActive(productDetails.getIsActive());
            productEntity.setImageUrl(productDetails.getImageUrl());
            productEntity.setIsBlend(productDetails.getIsBlend());
            productEntity.setRoastLevel(productDetails.getRoastLevel());
            productEntity.setAcidityScore(productDetails.getAcidityScore());
            log.info("Updating product with id {}: {}", id, productEntity.getName());
            return productRepository.save(productEntity);
        } catch (EntityNotFoundException e) {
            log.error("Product with id {} not found for update", id, e);
            throw new ProductNotAvailableException("Product not found!", e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) throws ServiceException {
        try {
            log.info("Deleting product with id: {}", id);
            productRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            log.error("Error deleting product with id: {}", id, e);
            throw new ServiceException("Error deleting product", e);
        } catch (OptimisticLockingFailureException e) {
            log.error("Optimistic locking failure while deleting product with id: {}", id, e);
            throw new ServiceException("Error deleting product due to concurrent modification", e);
        }
    }

    @Override
    public Page<Integer> getAllProductsReviewCount(Pageable pageable) throws BusinessException {
        log.info(
                "Fetching all products' review counts with pagination: page number {}, page size {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        List<Product> products = productRepository.findAll(pageable).getContent();
        List<Integer> reviewCounts = products.stream()
                .map(product -> reviewRepository.findAllByProductId(product.getId()).size())
                .toList();
        for (int i = 0; i < products.size(); i++) {
            log.info("Product ID: {}, Review Count: {}", products.get(i).getId(), reviewCounts.get(i));
        }
        return PageableExecutionUtils.getPage(
                reviewCounts,
                pageable,
                productRepository::count
        );
    }

    @Override
    public Page<Double> getAllProductsAverageRatings(Pageable pageable) throws BusinessException {
        log.info(
                "Fetching all products' average ratings with pagination: page number {}, page size {}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        List<Product> products = productRepository.findAll(pageable).getContent();

        List<Double> averageRatings = products.stream()
                .map(product -> {
                    if (product.getReviews() == null || product.getReviews().isEmpty()) {
                        return 0.0;
                    }
                    double avg = product.getReviews().stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);
                    return Math.round(avg * 100.0) / 100.0;
                })
                .toList();

        return PageableExecutionUtils.getPage(
                averageRatings,
                pageable,
                productRepository::count
        );
    }
}
