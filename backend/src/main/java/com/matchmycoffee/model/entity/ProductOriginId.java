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
public class ProductOriginId implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "origin_id")
    private Long originId;
}
