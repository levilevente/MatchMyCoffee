package com.matchmycoffee.controller;

import com.matchmycoffee.dto.response.blogpost.BlogPostDetailedResponse;
import com.matchmycoffee.dto.response.blogpost.BlogPostResponse;
import com.matchmycoffee.mapper.BlogPostMapper;
import com.matchmycoffee.model.entity.BlogPost;
import com.matchmycoffee.service.BlogPostService;
import com.matchmycoffee.service.exception.BlogPostNotFoundException;
import com.matchmycoffee.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/blogposts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private BlogPostMapper blogPostMapper;

    @GetMapping
    public ResponseEntity<Page<BlogPostResponse>> getAllBlogPosts(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size,
                                                                  @RequestParam(defaultValue = "id") String sortBy)
            throws ServiceException {
        log.info("GET /blogposts");

        if (size <= 0 || page < 0) {
            log.warn("Invalid pagination parameters: page={}, size={}", page, size);
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<BlogPostResponse> blogPostResponses = blogPostService.getAllBlogPosts(pageable).map(blogPostMapper::toDto);

        return ResponseEntity.ok(blogPostResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponse> getBlogPostById(@PathVariable Long id) throws BlogPostNotFoundException {
        log.info("GET /blogposts/{}", id);

        BlogPost blogPost = blogPostService.getBlogPostById(id);
        BlogPostDetailedResponse blogPostResponse = blogPostMapper.toDetailedDto(blogPost);

        return ResponseEntity.ok(blogPostResponse);
    }
}
