package com.aspire.lms;

import com.aspire.lms.enums.LoanStatus;
import com.aspire.lms.model.Loan;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class LoanApprovalTest {

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
    public void testApproveLoan_AlreadyPaid() {
        // Mock dependencies (loanRepository)
        // Create a Loan object with a paid status
        Loan loan = new Loan();
        loan.setLoanId("loan123");
        loan.setStatus(LoanStatus.PAID);

        // Mock the loanRepository.findByLoanId method to return the Loan object
        when(loanRepository.findByLoanId("loan123")).thenReturn(Optional.of(loan));

        // Call the approveLoan function and expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> loanService.approveLoan("loan123"));
    }

    @Test
    public void testApproveLoan_AlreadyApproved() {
        // Mock dependencies (loanRepository)
        // Create a Loan object with an approved status
        Loan loan = new Loan();
        loan.setLoanId("loan123");
        loan.setStatus(LoanStatus.APPROVED);

        // Mock the loanRepository.findByLoanId method to return the Loan object
        when(loanRepository.findByLoanId("loan123")).thenReturn(Optional.of(loan));

        // Call the approveLoan function and expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> loanService.approveLoan("loan123"));
    }


    @Test
    public void testApproveLoan_LoanNotFound() {
        // Mock dependencies (loanRepository)
        // Mock the loanRepository.findByLoanId method to return an empty Optional
        when(loanRepository.findByLoanId("nonExistentLoan")).thenReturn(Optional.empty());

        // Call the approveLoan function and expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> loanService.approveLoan("nonExistentLoan"));
    }

    @Test
    public void testApproveLoan_Success() {
        Loan loan = new Loan();
        loan.setLoanId("loan123");
        loan.setStatus(LoanStatus.PENDING);

        // Mock the loanRepository.findByLoanId method to return the Loan object
        when(loanRepository.findByLoanId("loan123")).thenReturn(Optional.of(loan));

        // Call the approveLoan function
        loanService.approveLoan("loan123");

        // Assertions
        assertEquals(LoanStatus.APPROVED, loan.getStatus());
        // Verify that the loanRepository.save method is called once
        verify(loanRepository, times(1)).save(loan);
    }

}
