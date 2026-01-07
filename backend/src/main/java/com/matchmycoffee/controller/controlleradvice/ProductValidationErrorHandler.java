package com.matchmycoffee.controller.controlleradvice;

import com.matchmycoffee.service.exception.IllegalProductArgumentException;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
import com.matchmycoffee.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    public static class ErrorResponse {
        private final Instant timestamp;
        private final int status;
        private final String error;
        private final String message;

        public ErrorResponse(Instant timestamp, int status, String error, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }
}
