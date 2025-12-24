package com.matchmycoffee.client.exception;

public class AgentTimeoutException extends RuntimeException {
    public AgentTimeoutException(String message) {
        super(message);
    }
}
