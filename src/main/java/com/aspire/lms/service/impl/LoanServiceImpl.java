package com.aspire.lms.service.impl;

import com.aspire.lms.dto.request.LoanRequest;
import com.aspire.lms.dto.request.RepaymentRequest;
import com.aspire.lms.enums.LoanStatus;
import com.aspire.lms.enums.RepaymentStatus;
import com.aspire.lms.model.Loan;
import com.aspire.lms.model.ScheduledRepayment;
import com.aspire.lms.model.User;
import com.aspire.lms.repository.LoanRepository;
import com.aspire.lms.repository.ScheduledRepaymentsRepository;
import com.aspire.lms.repository.UserRepository;
import com.aspire.lms.service.interfaces.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.aspire.lms.exception.Exception.record_not_found;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduledRepaymentsRepository repaymentsRepository;


    @Override
    public Loan createLoan(LoanRequest loanRequest) {

        //create a loan and generate repayments
        Loan loan = new Loan();

        User user = userRepository.findByUserId(loanRequest.getUserId()).orElseThrow(() -> new RuntimeException(record_not_found.getMessage()));
        loan.setUser(user);
        loan.setLoanTerm(loanRequest.getLoanTerm());
        //we will be setting all loans to a pending status till it gets approved
        loan.setStatus(LoanStatus.PENDING);
        loan.setAmountRequired(loanRequest.getAmountRequired());
        //loanId will be a random uuid, this is the id we will use for exposing to outside world as we do not want to expose id over network
        loan.setLoanId(UUID.randomUUID().toString());

        // Calculate the weekly repayment amount
        double weeklyRepayment = loanRequest.getAmountRequired() / loanRequest.getLoanTerm();

        // Calculate the start date as the current date
        LocalDate startDate = LocalDate.now();

        // Generate scheduled repayments for the loan term
        List<ScheduledRepayment> scheduledRepayments = new ArrayList<>();
        for (int i = 0; i < loanRequest.getLoanTerm(); i++) {
            ScheduledRepayment repayment = new ScheduledRepayment();
            repayment.setAmount(weeklyRepayment);
            repayment.setRepaymentId(UUID.randomUUID().toString());
            repayment.setDate(startDate.plusWeeks(i + 1)); // Schedule repayments on a weekly basis
            repayment.setStatus(RepaymentStatus.PENDING);
            repayment.setLoan(loan);
            scheduledRepayments.add(repayment);
        }
        loan.setScheduledRepayments(scheduledRepayments);
        loanRepository.save(loan);
        repaymentsRepository.saveAll(scheduledRepayments);
        return loan;
    }

    @Override
    public List<Loan> getLoansByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException(record_not_found.getMessage()));
        return loanRepository.findByUserId(user.getId());
    }

    @Override
    @Transactional
    public void approveLoan(String loanId) {
        Loan loan = loanRepository.findByLoanId(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));

        // Check if the loan is already approved or paid; if yes, throw an exception or handle accordingly
        if (loan.getStatus() == LoanStatus.APPROVED || loan.getStatus() == LoanStatus.PAID) {
            throw new RuntimeException("Loan is already approved or paid");
        }

        // Update the status of the loan to "APPROVED"
        loan.setStatus(LoanStatus.APPROVED);
        loanRepository.save(loan);
    }

    @Override
    @Transactional
    public void addRepayment(RepaymentRequest repaymentRequest) {

        Loan loan = loanRepository.findByLoanId(repaymentRequest.getLoanId()).orElseThrow(() -> new RuntimeException("Loan not found"));

        // Check if the loan is already paid; if yes, throw an exception or handle accordingly
        if (loan.getStatus() == LoanStatus.PAID) {
            throw new RuntimeException("Loan is already paid");
        }

        // Find the next pending scheduled repayment for the loan
        ScheduledRepayment nextScheduledRepayment = loan.getScheduledRepayments()
                .stream()
                .filter(repayment -> repayment.getStatus() == RepaymentStatus.PENDING)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No pending scheduled repayments found"));

        // Check if the repayment amount is greater than or equal to the scheduled amount
        if (repaymentRequest.getAmount() >= nextScheduledRepayment.getAmount()) {
            // Set the status of the scheduled repayment to "PAID"
            nextScheduledRepayment.setStatus(RepaymentStatus.PAID);
            repaymentsRepository.save(nextScheduledRepayment);

            // Check if all scheduled repayments for the loan are "PAID"
            boolean allRepaymentsPaid = loan.getScheduledRepayments()
                    .stream()
                    .allMatch(repayment -> repayment.getStatus() == RepaymentStatus.PAID);

            // If all repayments are "PAID", update the loan status to "PAID"
            if (allRepaymentsPaid) {
                loan.setStatus(LoanStatus.PAID);
                loanRepository.save(loan);
            }
        } else {
            // Handle the case where the repayment amount is less than the scheduled amount
            throw new RuntimeException("Repayment amount is less than the scheduled amount");
        }
    }

}
