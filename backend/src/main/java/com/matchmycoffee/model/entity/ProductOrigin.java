package com.matchmycoffee.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "product_origins")
public class ProductOrigin {

    @EmbeddedId
    private ProductOriginId id = new ProductOriginId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("originId")
    @JoinColumn(name = "origin_id")
    private Origin origin;

    @Column(name = "percentage")
    private Integer percentage;

}
