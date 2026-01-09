package com.matchmycoffee.dto.response.product;

import com.matchmycoffee.dto.response.BrewingMethodResponse;
import com.matchmycoffee.dto.response.OriginResponse;
import com.matchmycoffee.dto.response.TasteResponse;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDetailResponse extends ProductSummaryResponse {
    private Boolean isActive;

    private ProductSpecifications specifications;

    private List<OriginResponse> origins;
    private List<TasteResponse> tastes;
    private List<BrewingMethodResponse> brewingMethods;
}
