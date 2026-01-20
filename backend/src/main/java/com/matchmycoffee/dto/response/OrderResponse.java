package com.matchmycoffee.dto.response;

import com.matchmycoffee.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderResponse {
    private Long id;
    private String orderReference;
    private OrderStatus status;
    private Double totalAmount;
    private String currency;

    private String customerEmail;
    private String customerFirstName;
    private String customerLastName;

    private String shippingAddressLine1;
    private String shippingAddressLine2;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private String shippingCountry;

    private List<OrderItemResponse> items;
}
