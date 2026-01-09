package com.matchmycoffee.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_tastes")
public class ProductTaste {

    @EmbeddedId
    private ProductTasteId id = new ProductTasteId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tasteId")
    @JoinColumn(name = "taste_id", nullable = false)
    private Taste taste;
}
