package com.matchmycoffee.mapper;

import com.matchmycoffee.dto.response.blogpost.BlogPostDetailedResponse;
import com.matchmycoffee.dto.response.blogpost.BlogPostResponse;
import com.matchmycoffee.model.entity.BlogPost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlogPostMapper {

    BlogPostResponse toDto(BlogPost blogPost);

    BlogPostDetailedResponse toDetailedDto(BlogPost blogPost);
}
