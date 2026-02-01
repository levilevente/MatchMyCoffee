package com.matchmycoffee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class OriginResponse {
    private Long id;
    private String region;
    private String continent;
    private Integer percentage;
}
