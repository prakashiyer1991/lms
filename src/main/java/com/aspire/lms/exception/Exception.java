package com.aspire.lms.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Exception {
    record_not_found(HttpStatus.NOT_FOUND, "Record not found",1),
    loan_already_approved(HttpStatus.BAD_REQUEST, "Loan already approved",2);

    private HttpStatus statusCode;
    private String message;
    private int successCode;

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public int getSuccessCode() {
        return this.successCode;
    }

    private Exception(HttpStatus statusCode, String message, int successCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.successCode = successCode;
    }
}
