package com.matchmycoffee.service.exception;

public class BlogPostNotFoundException extends Exception {
    public BlogPostNotFoundException(String message) {
        super(message);
    }

    public BlogPostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
