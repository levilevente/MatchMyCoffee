package com.matchmycoffee.model.entity;

import com.matchmycoffee.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(name = "order_reference", nullable = false, length = 50, unique = true)
    private String orderReference;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "currency", length = 3, columnDefinition = "VARCHAR(3) DEFAULT 'RON'")
    private String currency;

    @Column(
            name = "status",
            nullable = false,
            length = 20,
            columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'"
    )
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "customer_email", nullable = false, length = 150)
    private String customerEmail;

    @Column(name = "customer_first_name", nullable = false, length = 100)
    private String customerFirstName;

    @Column(name = "customer_last_name", nullable = false, length = 100)
    private String customerLastName;

    @Column(name = "shipping_address_line1", nullable = false, length = 255)
    private String shippingAddressLine1;

    @Column(name = "shipping_address_line2", length = 255)
    private String shippingAddressLine2;

    @Column(name = "shipping_city", nullable = false, length = 100)
    private String shippingCity;

    @Column(name = "shipping_state", length = 100)
    private String shippingState;

    @Column(name = "shipping_zip", nullable = false, length = 20)
    private String shippingZip;

    @Column(name = "shipping_country", nullable = false, length = 100)
    private String shippingCountry;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();
}
