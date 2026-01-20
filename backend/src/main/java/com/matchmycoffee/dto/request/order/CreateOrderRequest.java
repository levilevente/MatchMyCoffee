package com.matchmycoffee.dto.request.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateOrderRequest {

    @NotNull
    @Email
    private String customerEmail;

    @NotNull
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String customerFirstName;

    @NotNull
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String customerLastName;

    @NotNull
    @Size(min = 10, max = 255, message = "Shipping address must be between 10 and 255 characters")
    private String shippingAddressLine1;

    @Size(max = 255, message = "Shipping address line 2 must be at most 255 characters")
    private String shippingAddressLine2;

    @NotNull
    @Size(min = 3, max = 100, message = "Shipping city must be between 3 and 100 characters")
    private String shippingCity;

    @NotNull
    @Size(min = 2, max = 2, message = "Shipping state must be between 2 and 2 characters")
    private String shippingState;

    @NotNull
    @Size(min = 5, max = 6, message = "Shipping zip must be between 5 and 6 characters")
    private String shippingZip;

    @NotNull
    @Size(min = 2, max = 2, message = "Shipping country must be 2 characters")
    private String shippingCountry;

    @Valid
    private List<OrderItemRequest> items;
}
