package com.matchmycoffee.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class ProductBrewingMethodId implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "brewing_method_id")
    private Integer brewingMethodId;
}