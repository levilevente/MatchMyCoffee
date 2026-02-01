package com.matchmycoffee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class BrewingMethodResponse {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private Boolean isOptimal;
}
