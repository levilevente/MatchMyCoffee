package com.matchmycoffee.service.impl;

import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.repository.ProductRepository;
import com.matchmycoffee.service.ProductService;
import com.matchmycoffee.service.exception.IllegalProductArgumentException;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
import com.matchmycoffee.service.exception.ServiceException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product getProductById(Long id) throws ProductNotAvailableException {
        log.info("Getting product by id: {}", id);
        try {
            Product product = productRepository.findByIdWithDetails(id).orElseThrow(() -> {
                log.error("Product with id {} not found", id);
                return new ProductNotAvailableException("Product not found!");
            });

            List<Object[]> statsList = productRepository.findProductStats(id);

            if (!statsList.isEmpty()) {
                Object[] row = statsList.get(0); // Unwrap the first row
                Long count = (Long) row[0];
                Double avg = (row[1] != null) ? (Double) row[1] : 0.0;
                avg = Math.round(avg * 100.0) / 100.0;

                product.setCalculatedReviewCount(count);
                product.setCalculatedAverageRating(avg);
            }

            return product;
        } catch (EntityNotFoundException e) {
            log.error("Product with id {} not found", id, e);
            throw new ProductNotAvailableException("Product not found!", e);
        }
    }

    @Override
    public Optional<Product> findProductByName(String name) throws ServiceException {
        log.info("Finding product by name: {}", name);
        return Optional.ofNullable(productRepository.findByName(name));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) throws ServiceException {
        log.info("Fetching all products with pagination: page number {}, page size {}", pageable.getPageNumber(),
                pageable.getPageSize());

        Page<Object[]> productDataPage = productRepository.findAllWithReviewStats(pageable);
        return productDataPage.map(objects -> {
            Product product = (Product) objects[0];
            Long reviewCount = (Long) objects[1];
            Double averageRating = (Double) objects[2];

            product.setCalculatedReviewCount(reviewCount != null ? reviewCount : 0);
            product.setCalculatedAverageRating(averageRating != null ? averageRating : 0.0);

            return product;
        });
    }

    @Override
    @Transactional
    public Product createProduct(Product product) throws IllegalProductArgumentException, ServiceException {
        if (product.getAcidityScore() > 5 || product.getAcidityScore() < 1) {
            log.error("Acidity score {} is out of bounds. It should be between 1 and 5.", product.getAcidityScore());
            throw new IllegalProductArgumentException("Acidity score must be between 1 and 5.");
        }

        if (product.getRoastLevel() > 5 || product.getRoastLevel() < 1) {
            log.error("Roast level {} is out of bounds. It should be between 1 and 5.", product.getRoastLevel());
            throw new IllegalProductArgumentException("Roast level must be between 1 and 5.");
        }

        log.info("Creating new product: {}", product.getName());
        Product savedProduct = productRepository.save(product);

        try {
            return this.getProductById(savedProduct.getId());
        } catch (ProductNotAvailableException e) {
            throw new ServiceException("Failed to retrieve the created product.", e);
        }
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product productDetails) throws ProductNotAvailableException {
        try {
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
}
