package com.matchmycoffee.dto.response.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewResponse {
    private Long id;
    private String authorName;
    private Integer rating;
    private String comment;
    private Boolean isApproved;
}
