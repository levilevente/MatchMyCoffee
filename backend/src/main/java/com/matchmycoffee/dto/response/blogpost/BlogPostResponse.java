package com.matchmycoffee.dto.response.blogpost;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogPostResponse {
    private Long id;
    private String title;
    private String publishedAt;
    private Boolean isPublished;
}
