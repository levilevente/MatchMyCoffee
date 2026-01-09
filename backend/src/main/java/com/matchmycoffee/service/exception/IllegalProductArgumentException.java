package com.matchmycoffee.service.exception;

public class IllegalProductArgumentException extends Exception {
    public IllegalProductArgumentException(String message) {
        super(message);
    }

    public IllegalProductArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
