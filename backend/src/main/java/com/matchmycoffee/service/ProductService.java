package com.matchmycoffee.service;

import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.service.exception.IllegalProductArgumentException;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
import com.matchmycoffee.service.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {
    Product getProductById(Long id) throws ProductNotAvailableException;

    Optional<Product> findProductByName(String name) throws ServiceException;

    Page<Product> getAllProducts(Pageable pageable) throws ServiceException;

    Product createProduct(Product product) throws IllegalProductArgumentException, ServiceException;

    Product updateProduct(Long id, Product productDetails) throws ProductNotAvailableException;

    void deleteProduct(Long id) throws ServiceException;
}
