package com.matchmycoffee.service.impl;

import com.matchmycoffee.model.entity.BlogPost;
import com.matchmycoffee.repository.BlogPostRepository;
import com.matchmycoffee.service.BlogPostService;
import com.matchmycoffee.service.exception.BlogPostNotFoundException;
import com.matchmycoffee.service.exception.ServiceException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;

    public BlogPostServiceImpl(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public BlogPost getBlogPostById(Long id) throws BlogPostNotFoundException {
        log.info("Getting blog post by id: {}", id);
        try {
            return blogPostRepository.findById(id).orElseThrow(() -> {
                log.error("Blog post with id {} not found", id);
                return new BlogPostNotFoundException("Blog post not found!");
            });
        } catch (EntityNotFoundException e) {
            log.error("Error while retrieving blog post with id {}", id, e);
            throw new BlogPostNotFoundException("Blog post not found!", e);
        }
    }

    @Override
    public Page<BlogPost> getAllBlogPosts(Pageable pageable) throws ServiceException {
        log.info("Getting all blog posts");
        try {
            return blogPostRepository.findAll(pageable);
        } catch (EntityNotFoundException e) {
            log.error("Error while retrieving blog posts", e);
            throw new ServiceException("Failed to retrieve blog posts", e);
        }
    }

    @Override
    public BlogPost createBlogPost(BlogPost blogPost) throws ServiceException {
        return null;
    }

    @Override
    public BlogPost updateBlogPost(Long id, BlogPost blogPostDetails) throws BlogPostNotFoundException {
        return null;
    }

    @Override
    public void deleteBlogPost(Long id) throws ServiceException {

    }
}
