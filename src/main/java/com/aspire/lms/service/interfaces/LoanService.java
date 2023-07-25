package com.aspire.lms.service.interfaces;


import com.aspire.lms.dto.request.LoanRequest;
import com.aspire.lms.dto.request.RepaymentRequest;
import com.aspire.lms.model.Loan;

import java.util.List;

public interface LoanService {

    Loan createLoan(LoanRequest loanRequest);

    List<Loan> getLoansByUserId(String userId);



    void approveLoan(String loanId);

    void addRepayment(RepaymentRequest repaymentRequest);
}
