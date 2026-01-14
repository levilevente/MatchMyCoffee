package com.matchmycoffee.dto.request.review;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewRequest {

    @NotNull(message = "Author name must not be null")
    @Size(min = 3, max = 255, message = "Author name must be between 3 and 255 characters")
    private String authorName;

    @NotNull(message = "Rating must not be null")
    private Integer rating;

    @NotNull(message = "Comment must not be null")
    @Size(max = 1000, message = "Comment must be at most 1000 characters")
    private String comment;
}
