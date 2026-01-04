package com.matchmycoffee.model.entity;

import jakarta.persistence.Column;

import java.io.Serializable;

public class ProductTasteId implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "taste_id")
    private Integer tasteId;
}
