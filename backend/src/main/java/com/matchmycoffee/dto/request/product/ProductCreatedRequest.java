package com.matchmycoffee.dto.request.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductCreatedRequest {

    @NotNull
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotNull
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotNull
    @Positive(message = "Stock must be a positive integer")
    private Integer stock;

    @NotNull
    private Boolean isActive;

    @NotNull
    @Size(max = 255, message = "Image URL must be at most 255 characters")
    private String imageUrl;

    @NotNull
    private Boolean isBlend;

    @NotNull
    @Positive(message = "Roast level must be a positive integer")
    private Integer roastLevel;

    @NotNull
    @Positive(message = "Acidity score must be a positive integer")
    private Integer acidityScore;
}
