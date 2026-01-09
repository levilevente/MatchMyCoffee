package com.matchmycoffee.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@Table(name = "product_brewing_methods")
public class ProductBrewingMethod {

    @EmbeddedId
    private ProductBrewingMethodId id = new ProductBrewingMethodId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("brewingMethodId")
    @JoinColumn(name = "brewing_method_id")
    private BrewingMethod brewingMethod;

    @Column(name = "is_optimal", nullable = false)
    private Boolean isOptimal = false;
}
