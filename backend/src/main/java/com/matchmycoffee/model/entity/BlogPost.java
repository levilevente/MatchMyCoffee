package com.matchmycoffee.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "blog_posts")
public class BlogPost extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "author_role", nullable = false, length = 50, columnDefinition = "VARCHAR(50) DEFAULT 'manager'")
    private String authorRole;

    @Column(name = "published_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime publishedAt;

    @Column(name = "is_published", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isPublished;

}
