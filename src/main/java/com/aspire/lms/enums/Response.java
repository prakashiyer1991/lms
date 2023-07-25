package com.aspire.lms.enums;
import org.springframework.http.HttpStatus;

public enum Response {
    loan_retrieved_successfully(HttpStatus.OK, "Loan retrieved successfully", 1),
    loan_created_successfully(HttpStatus.CREATED, "Loan created successfully", 2),
    loan_repayment_processed_successfully(HttpStatus.OK, "Loan repayment processed successfully", 3),
    loan_processed_successfully(HttpStatus.OK, "Loan approval processed successfully", 4),
    record_deleted_successfully(HttpStatus.OK, "Record deleted successfully", 5);

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

    private Response(HttpStatus statusCode, String message, int successCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.successCode = successCode;
    }
}
