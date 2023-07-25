package com.aspire.lms.controller;

import com.aspire.lms.dto.request.LoanRequest;
import com.aspire.lms.dto.request.RepaymentRequest;
import com.aspire.lms.dto.response.LoanResponse;
import com.aspire.lms.service.interfaces.LoanService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.aspire.lms.enums.Response.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Timed
    @PostMapping(path = "create",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody
    ResponseEntity<LoanResponse> create(
            @Valid @RequestBody LoanRequest loanRequest) {

        return ResponseEntity.status(loan_created_successfully.getStatusCode())
                .body(LoanResponse.builder().data(loanService.createLoan(loanRequest)).response(loan_created_successfully).build());
    }

    @Timed
    @PostMapping(path = "repayment",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody
    ResponseEntity addRepayment(
            @Valid @RequestBody RepaymentRequest repaymentRequest) {
        loanService.addRepayment(repaymentRequest);
        return ResponseEntity.status(loan_repayment_processed_successfully.getStatusCode())
                .body(LoanResponse.builder().response(loan_repayment_processed_successfully).build());
    }

    @Timed
    @GetMapping(path = "get",
            produces =  {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public @ResponseBody
    ResponseEntity<LoanResponse> get(
            @NotNull @RequestHeader(name = "X-USER-ID") String userId) {
            return ResponseEntity.status(loan_retrieved_successfully.getStatusCode())
                    .body(LoanResponse.builder().data(loanService.getLoansByUserId(userId)).response(loan_retrieved_successfully).build());
    }


}
