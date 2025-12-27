package com.matchmycoffee.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ProductSummaryResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Boolean isBlend;
    private Integer roastLevel;
    private Double averageRating;
    private Integer reviewCount;
}
