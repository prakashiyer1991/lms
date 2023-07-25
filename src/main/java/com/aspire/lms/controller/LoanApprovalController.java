package com.aspire.lms.controller;

import com.aspire.lms.dto.response.LoanResponse;
import com.aspire.lms.service.interfaces.LoanService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;

import static com.aspire.lms.enums.Response.loan_processed_successfully;

@RestController
@RequestMapping("/api/admin/loans")
public class LoanApprovalController {

    @Autowired
    LoanService loanService;

    @Timed
    @PostMapping(path = "approve",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody
    ResponseEntity<LoanResponse> approve(
            @NotNull @RequestHeader(name = "LOAN-ID") String loanId) {
        loanService.approveLoan(loanId);
        return ResponseEntity.status(loan_processed_successfully.getStatusCode())
                .body(LoanResponse.builder().response(loan_processed_successfully).build());
    }

}
