package com.matchmycoffee.controller;

import com.matchmycoffee.dto.response.product.ProductDetailResponse;
import com.matchmycoffee.dto.response.product.ProductSummaryResponse;
import com.matchmycoffee.mapper.ProductMapper;
import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.service.ProductService;
import com.matchmycoffee.service.exception.BusinessException;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<Page<ProductSummaryResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) throws BusinessException {
        log.info("GET /products");

        if (!"id".equals(sortBy) && !"name".equals(sortBy) && !"price".equals(sortBy)) {
            log.warn("Invalid sortBy parameter: {}", sortBy);
            return ResponseEntity.badRequest().build();
        }

        if (size <= 0 || page < 0) {
            log.warn("Invalid pagination parameters: page={}, size={}", page, size);
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Product> productPage = productService.getAllProducts(pageable);

        Page<ProductSummaryResponse> responsePage = productPage.map(productMapper::toProductSummaryResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductById(
            @PathVariable Long id
    ) throws ProductNotAvailableException {
        log.info("GET /products/{}", id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.toProductDetailResponse(product));
    }
}
