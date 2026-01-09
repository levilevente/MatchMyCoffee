package com.matchmycoffee.dto.response.blogpost;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class BlogPostDetailedResponse extends BlogPostResponse {
    private String content;
    private String authorRole;
}
