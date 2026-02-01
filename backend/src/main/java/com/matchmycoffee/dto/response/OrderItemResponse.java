package com.matchmycoffee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemResponse {
    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double priceAtPurchase;
}
