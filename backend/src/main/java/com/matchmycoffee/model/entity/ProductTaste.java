package com.matchmycoffee.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "product_tastes")
public class ProductTaste extends AbstractModel {

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
