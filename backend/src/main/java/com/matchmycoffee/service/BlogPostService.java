package com.matchmycoffee.service;

import com.matchmycoffee.model.entity.BlogPost;
import com.matchmycoffee.service.exception.BlogPostNotFoundException;
import com.matchmycoffee.service.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogPostService {
    BlogPost getBlogPostById(Long id) throws BlogPostNotFoundException;

    Page<BlogPost> getAllBlogPosts(Pageable pageable) throws ServiceException;

    BlogPost createBlogPost(BlogPost blogPost) throws ServiceException;

    BlogPost updateBlogPost(Long id, BlogPost blogPostDetails) throws BlogPostNotFoundException;

    void deleteBlogPost(Long id) throws ServiceException;

}
