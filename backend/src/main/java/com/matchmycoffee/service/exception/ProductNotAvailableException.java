package com.matchmycoffee.service.exception;

public class ProductNotAvailableException extends Exception {
    public ProductNotAvailableException(String message) {
        super(message);
    }

    public ProductNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
