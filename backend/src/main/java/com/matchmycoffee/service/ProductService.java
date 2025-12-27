package com.matchmycoffee.service;

import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.service.exception.BusinessException;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
import com.matchmycoffee.service.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Product getProductById(Long id);

    Optional<Product> findProductByName(String name) throws BusinessException;

    Page<Product> getAllProducts(Pageable pageable) throws BusinessException;

    Page<Integer> getAllProductsReviewCount(Pageable pageable) throws BusinessException;

    Page<Double> getAllProductsAverageRatings(Pageable pageable) throws BusinessException;

    Product createProduct(Product product);

    Product updateProduct(Long id, Product productDetails) throws ProductNotAvailableException;

    void deleteProduct(Long id) throws ServiceException;
}
