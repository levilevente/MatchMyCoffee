package com.matchmycoffee.dto.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductOriginRequest {
    @NotNull
    private Long id;

    @NotNull
    private String percentage;
}
