package com.giimhana.userManagement.exception.domain;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.giimhana.userManagement.domain.HttpResponse;

@RestControllerAdvice
public class ExceptionHandling {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please sned a '%s'";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occureed while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / Password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this isan error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You don not have enough permission";

    @ExceptionHandler(DisabledException.class)
    private ResponseEntity<HttpResponse> accountDisabledException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());

        return new ResponseEntity<>(httpResponse, httpStatus);
    }

}
