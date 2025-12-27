package com.matchmycoffee.controller;

import com.matchmycoffee.dto.response.product.ProductSummaryResponse;
import com.matchmycoffee.mapper.ProductMapper;
import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.service.ProductService;
import com.matchmycoffee.service.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Product> productPage = productService.getAllProducts(pageable);
        Page<Integer> productReviewCountPage = productService.getAllProductsReviewCount(pageable);
        Page<Double> productAverageRatingsPage = productService.getAllProductsAverageRatings(pageable);

        var products = productPage.getContent();
        var reviewCounts = productReviewCountPage.getContent();
        var averageRatings = productAverageRatingsPage.getContent();

        List<ProductSummaryResponse> responseList = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {
            Integer count = (i < reviewCounts.size()) ? reviewCounts.get(i) : 0;
            Double rating = (i < averageRatings.size()) ? averageRatings.get(i) : 0.0;

            responseList.add(productMapper.toProductSummaryResponse(products.get(i), rating, count));
        }

        Page<ProductSummaryResponse> responsePage = new PageImpl<>(
                responseList,
                pageable,
                productPage.getTotalElements()
        );

        return ResponseEntity.ok(responsePage);
    }
}
