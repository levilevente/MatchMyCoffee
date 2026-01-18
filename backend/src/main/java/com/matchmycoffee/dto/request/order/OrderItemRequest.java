package com.matchmycoffee.dto.request.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItemRequest {
    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @NotNull
    @Positive(message = "Quantity must be a positive integer")
    private Integer quantity;

    @NotNull
    @Positive(message = "Price must be a positive number")
    private Double priceAtPurchase;
}
