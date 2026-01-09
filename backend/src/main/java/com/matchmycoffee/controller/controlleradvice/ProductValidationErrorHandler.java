package com.matchmycoffee.controller.controlleradvice;

import com.matchmycoffee.service.exception.*;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class ProductValidationErrorHandler {
    private ErrorResponse buildErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final ErrorResponse handleServiceException(ServiceException e) {
        log.error("ServiceException occurred", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occurred.");
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final ErrorResponse handleProductNotAvailableException(ProductNotAvailableException e) {
        log.debug("ProductNotAvailableException occurred", e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalProductArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ErrorResponse handleIllegalProductArgumentException(IllegalProductArgumentException e) {
        log.debug("IllegalProductArgumentException occurred", e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final ErrorResponse handleReviewNotFoundException(ReviewNotFoundException e) {
        log.debug("ReviewNotFoundException occurred", e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BlogPostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final ErrorResponse handleBlogPostNotFoundException(BlogPostNotFoundException e) {
        log.debug("BlogPostNotFoundException occurred", e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public final ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        log.debug("MethodArgumentTypeMismatchException occurred", e);
        String message = String.format("Invalid value '%s' for parameter '%s'.", e.getValue(), e.getName());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
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
