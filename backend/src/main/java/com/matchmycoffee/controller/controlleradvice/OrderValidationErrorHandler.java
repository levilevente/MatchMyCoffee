package com.matchmycoffee.controller.controlleradvice;

import com.matchmycoffee.service.exception.IllegalOrderArgumentException;
import com.matchmycoffee.service.exception.InsufficientStockException;
import com.matchmycoffee.service.exception.InvalidOrderStateException;
import com.matchmycoffee.service.exception.OrderNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class OrderValidationErrorHandler {

    private ErrorResponse buildErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final ErrorResponse handleOrderNotFoundException(OrderNotFoundException e) {
        log.debug("OrderNotFoundException occurred", e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalOrderArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ErrorResponse handleIllegalOrderArgumentException(IllegalOrderArgumentException e) {
        log.debug("IllegalOrderArgumentException occurred", e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ErrorResponse handleInvalidOrderStateException(InvalidOrderStateException e) {
        log.debug("InvalidOrderStateException occurred", e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public final ErrorResponse handleInsufficientStockException(InsufficientStockException e) {
        log.debug("InsufficientStockException occurred", e);
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private final Instant timestamp;
        private final int status;
        private final String error;
        private final String message;
    }
}
