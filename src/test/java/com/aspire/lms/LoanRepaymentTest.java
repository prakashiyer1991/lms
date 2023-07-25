package com.aspire.lms;

import com.aspire.lms.dto.request.RepaymentRequest;
import com.aspire.lms.enums.LoanStatus;
import com.aspire.lms.enums.RepaymentStatus;
import com.aspire.lms.model.Loan;
import com.aspire.lms.model.ScheduledRepayment;
import com.aspire.lms.repository.LoanRepository;
import com.aspire.lms.repository.ScheduledRepaymentsRepository;
import com.aspire.lms.repository.UserRepository;
import com.aspire.lms.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith({MockitoExtension.class})
class LoanRepaymentTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ScheduledRepaymentsRepository repaymentsRepository;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRepayment_SuccessfulRepayment() {

        Loan loan = new Loan();
        loan.setLoanId("loan123");
        loan.setStatus(LoanStatus.PENDING);

        ScheduledRepayment scheduledRepayment1 = new ScheduledRepayment();
        scheduledRepayment1.setStatus(RepaymentStatus.PENDING);
        scheduledRepayment1.setAmount(100.0);

        ScheduledRepayment scheduledRepayment2 = new ScheduledRepayment();
        scheduledRepayment2.setStatus(RepaymentStatus.PENDING);
        scheduledRepayment2.setAmount(200.0);

        List<ScheduledRepayment> scheduledRepayments = List.of(scheduledRepayment1, scheduledRepayment2);
        loan.setScheduledRepayments(scheduledRepayments);

        when(loanRepository.findByLoanId("loan123")).thenReturn(Optional.of(loan));

        // Create a RepaymentRequest with the right amount and loanId
        RepaymentRequest repaymentRequest = new RepaymentRequest();
        repaymentRequest.setLoanId("loan123");
        repaymentRequest.setAmount(100.0);


        loanService.addRepayment(repaymentRequest);

        // Assertions
        assertEquals(RepaymentStatus.PAID, scheduledRepayment1.getStatus());
        assertEquals(RepaymentStatus.PENDING, scheduledRepayment2.getStatus());
        assertEquals(LoanStatus.PENDING, loan.getStatus());

        verify(repaymentsRepository, times(1)).save(scheduledRepayment1);

        verify(loanRepository, never()).save(loan);
    }


    @Test
    public void testAddRepayment_LoanNotFound() {
        when(loanRepository.findByLoanId("nonExistentLoan")).thenReturn(Optional.empty());


        RepaymentRequest repaymentRequest = new RepaymentRequest();
        repaymentRequest.setLoanId("nonExistentLoan");
        repaymentRequest.setAmount(100.0);

        assertThrows(RuntimeException.class, () -> loanService.addRepayment(repaymentRequest));
    }

    @Test
    public void testAddRepayment_UnsuccessfulRepayment() {
        Loan loan = new Loan();
        loan.setLoanId("loan123");
        loan.setStatus(LoanStatus.PENDING);

        ScheduledRepayment scheduledRepayment = new ScheduledRepayment();
        scheduledRepayment.setStatus(RepaymentStatus.PENDING);
        scheduledRepayment.setAmount(100.0);

        loan.setScheduledRepayments(List.of(scheduledRepayment));


        when(loanRepository.findByLoanId("loan123")).thenReturn(Optional.of(loan));

        // Create a RepaymentRequest with an amount less than the scheduled amount
        RepaymentRequest repaymentRequest = new RepaymentRequest();
        repaymentRequest.setLoanId("loan123");
        repaymentRequest.setAmount(50.0); // Repayment amount is less than the scheduled amount

        // Call the addRepayment function and expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> loanService.addRepayment(repaymentRequest));
    }

    @Test
    public void testAddRepayment_LoanAlreadyPaid() {
        Loan loan = new Loan();
        loan.setLoanId("loan123");
        loan.setStatus(LoanStatus.PAID);

        when(loanRepository.findByLoanId("loan123")).thenReturn(Optional.of(loan));

        // Create a RepaymentRequest with the right amount and loanId
        RepaymentRequest repaymentRequest = new RepaymentRequest();
        repaymentRequest.setLoanId("loan123");
        repaymentRequest.setAmount(100.0);

        // Call the addRepayment function and expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> loanService.addRepayment(repaymentRequest));
    }



}
