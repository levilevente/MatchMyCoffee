package com.matchmycoffee.dto.request.review;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewUpdateRequest {
    @Size(min = 3, max = 255, message = "Author name must be between 3 and 255 characters")
    private String authorName;

    private Integer rating;

    @Size(max = 1000, message = "Comment must be at most 1000 characters")
    private String comment;
    
    private Boolean isApproved;
}
