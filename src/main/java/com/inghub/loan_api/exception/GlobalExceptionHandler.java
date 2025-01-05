package com.inghub.loan_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProblemDetailsException.class)
    public ProblemDetail handleProblemDetailsException(ProblemDetailsException e) {
        return e.getProblemDetail();
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setInstance(URI.create("/errors/internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}