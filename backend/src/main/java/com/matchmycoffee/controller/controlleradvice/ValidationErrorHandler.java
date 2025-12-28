package com.matchmycoffee.controller.controlleradvice;

import com.matchmycoffee.service.exception.BusinessException;
import com.matchmycoffee.service.exception.ProductNotAvailableException;
import com.matchmycoffee.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ValidationErrorHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final String handleBusinessException(BusinessException e) {
        log.error("Exception occurred", e);
        return "An internal server error occurred.";
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public final String handleServiceException(ServiceException e) {
        log.error("ServiceException occurred", e);
        return "An internal server error occurred.";
    }

    @ExceptionHandler(ProductNotAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public final String handleProductNotAvailableException(ProductNotAvailableException e) {
        log.debug("ProductNotAvailableException occurred", e);
        return e.getMessage();
    }
}
