package com.matchmycoffee.service.exception;

public class IllegalReviewArgumentException extends Exception {
    public IllegalReviewArgumentException(String message) {
        super(message);
    }

    public IllegalReviewArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
