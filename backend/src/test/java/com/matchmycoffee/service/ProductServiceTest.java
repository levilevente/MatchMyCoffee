package com.matchmycoffee.service;

import com.matchmycoffee.model.entity.Product;
import com.matchmycoffee.repository.ProductRepository;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@SuppressWarnings("PMD")
class ProductServiceTest {

    private Product product1;
    private Product product2;

    @Autowired
    private ProductService productService;

    @MockitoBean
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Ethiopian Yirgacheffe");
        product1.setDescription("Bright and fruity coffee from Ethiopia");
        product1.setPrice(15.99);
        product1.setStock(100);
        product1.setIsActive(true);
        product1.setRoastLevel(3);
        product1.setAcidityScore(8);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Colombian Supremo");
        product2.setDescription("Smooth and balanced Colombian coffee");
        product2.setPrice(12.99);
        product2.setStock(50);
        product2.setIsActive(true);
        product2.setRoastLevel(4);
        product2.setAcidityScore(5);
    }

    @Test
    void testGetProductById_Success() throws ProductNotAvailableException {
        // Given
        given(productRepository.findByIdWithDetails(1L)).willReturn(Optional.of(product1));

        Object[] stats = new Object[]{5L, 4.5};
        List<Object[]> statsList = Collections.singletonList(stats);
        given(productRepository.findProductStats(1L)).willReturn(statsList);

        // When
        Product result = productService.getProductById(1L);

        // Then
        assertNotNull(result, "The returned product should not be null");
        assertEquals(1L, result.getId(), "The product ID should match the requested ID");
        assertEquals("Ethiopian Yirgacheffe", result.getName(), "The product name should match");
        assertEquals(15.99, result.getPrice(), "The product price should match");
        assertEquals(100, result.getStock(), "The product stock should match");
        assertTrue(result.getIsActive(), "The product should be active");
    }

    @Test
    void testGetProductById_NotFound() {
        // Given
        given(productRepository.findByIdWithDetails(999L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotAvailableException.class, () ->
            productService.getProductById(999L),
            "Expected ProductNotAvailableException to be thrown when product is not found"
        );
    }

    @Test
    void testGetAllProducts_Success() throws ServiceException {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Object[] product1Data = new Object[]{product1, 5L, 4.5};
        Object[] product2Data = new Object[]{product2, 3L, 4.0};
        List<Object[]> productDataList = List.of(product1Data, product2Data);
        Page<Object[]> productDataPage = new PageImpl<>(productDataList, pageable, productDataList.size());

        given(productRepository.findAllWithReviewStats(pageable)).willReturn(productDataPage);

        // When
        Page<Product> result = productService.getAllProducts(pageable);

        // Then
        assertNotNull(result, "The returned product page should not be null");
        assertEquals(2, result.getTotalElements(), "The total number of products should match");
        assertEquals("Ethiopian Yirgacheffe", result.getContent().get(0).getName(), "First product name should match");
        assertEquals("Colombian Supremo", result.getContent().get(1).getName(), "Second product name should match");
    }
}
