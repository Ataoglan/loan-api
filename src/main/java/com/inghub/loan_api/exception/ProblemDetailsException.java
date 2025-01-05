package com.inghub.loan_api.exception;

import org.springframework.http.ProblemDetail;

public class ProblemDetailsException extends RuntimeException {
    private final ProblemDetail problemDetail;

    public ProblemDetailsException(ProblemDetail problemDetail) {
        super(problemDetail.getDetail());
        this.problemDetail = problemDetail;
    }

    public ProblemDetail getProblemDetail() {
        return problemDetail;
    }
}