package com.matchmycoffee.service;

import com.matchmycoffee.model.entity.BlogPost;
import com.matchmycoffee.repository.BlogPostRepository;
import com.matchmycoffee.service.exception.BlogPostNotFoundException;
import com.matchmycoffee.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@SuppressWarnings("PMD")
class BlogPostServiceTest {

    private BlogPost blogPost1;
    private BlogPost blogPost2;
    private BlogPost blogPost3;

    @Autowired
    private BlogPostService blogPostService;

    @MockitoBean
    private BlogPostRepository blogPostRepository;

    @BeforeEach
    public void setup() {
        blogPost1 = new BlogPost();
        blogPost1.setId(1L);
        blogPost1.setTitle("First Blog Post");
        blogPost1.setContent("This is the content of the first blog post");
        blogPost1.setAuthorRole("manager");
        blogPost1.setIsPublished(true);
        blogPost1.setPublishedAt(LocalDateTime.now());

        blogPost2 = new BlogPost();
        blogPost2.setId(2L);
        blogPost2.setTitle("Second Blog Post");
        blogPost2.setContent("This is the content of the second blog post");
        blogPost2.setAuthorRole("admin");
        blogPost2.setIsPublished(false);
        blogPost2.setPublishedAt(LocalDateTime.now());

        blogPost3 = new BlogPost();
        blogPost3.setId(3L);
        blogPost3.setTitle("Third Blog Post");
        blogPost3.setContent("This is the content of the third blog post");
        blogPost3.setAuthorRole("manager");
        blogPost3.setIsPublished(true);
        blogPost3.setPublishedAt(LocalDateTime.now());
    }

    @Test
    public void testGetBlogPostById_Success() throws BlogPostNotFoundException {
        // Given
        given(blogPostRepository.findById(1L)).willReturn(Optional.of(blogPost1));

        // When
        BlogPost result = blogPostService.getBlogPostById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId(), "Blog post ID should match");
        assertEquals("First Blog Post", result.getTitle(), "Blog post title should match");
        assertEquals(
                "This is the content of the first blog post",
                result.getContent(),
                "Blog post content should match"
        );
        assertEquals("manager", result.getAuthorRole(), "Author role should match");
        assertTrue(result.getIsPublished(), "Blog post should be published");
    }

    @Test
    public void testGetBlogPostById_NotFound() {
        // Given
        given(blogPostRepository.findById(999L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(BlogPostNotFoundException.class, () ->
            blogPostService.getBlogPostById(999L), "Blog post not found!"
        );
    }

    @Test
    public void testGetAllBlogPosts_Success() throws ServiceException {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<BlogPost> blogPosts = List.of(blogPost1, blogPost2, blogPost3);
        Page<BlogPost> blogPostPage = new PageImpl<>(blogPosts, pageable, blogPosts.size());

        given(blogPostRepository.findAll(pageable)).willReturn(blogPostPage);

        // When
        Page<BlogPost> result = blogPostService.getAllBlogPosts(pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getTotalElements(), "Total elements should be 3");
        assertEquals(3, result.getContent().size(), "Content size should be 3");
        assertEquals("First Blog Post", result.getContent().get(0).getTitle(), "First blog post title should match");
        assertEquals("Second Blog Post", result.getContent().get(1).getTitle(), "Second blog post title should match");
        assertEquals("Third Blog Post", result.getContent().get(2).getTitle(), "Third blog post title should match");
    }

    @Test
    public void testGetAllBlogPosts_EmptyPage() throws ServiceException {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<BlogPost> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        given(blogPostRepository.findAll(pageable)).willReturn(emptyPage);

        // When
        Page<BlogPost> result = blogPostService.getAllBlogPosts(pageable);

        // Then
        assertNotNull(result, "Resulting page should not be null");
        assertEquals(0, result.getTotalElements(), "Total elements should be 0");
        assertTrue(result.getContent().isEmpty(), "Content should be empty");
    }
}

