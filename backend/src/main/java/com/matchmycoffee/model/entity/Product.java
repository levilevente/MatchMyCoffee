package com.matchmycoffee.model.entity;

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
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_blend", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isBlend;

    @Column(name = "roast_level", nullable = false)
    private Integer roastLevel;

    @Column(name = "acidity_score")
    private Integer acidityScore;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Transient
    private Long calculatedReviewCount;

    @Transient
    private Double calculatedAverageRating;
}
