package com.matchmycoffee.service.exception;

public class IllegalOrderArgumentException extends Exception{
    public IllegalOrderArgumentException(String message) {super(message);}

    public IllegalOrderArgumentException(String message, Throwable cause) {super(message, cause);}
}
