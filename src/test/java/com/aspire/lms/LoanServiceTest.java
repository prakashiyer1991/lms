package com.aspire.lms;

import com.aspire.lms.dto.request.LoanRequest;
import com.aspire.lms.enums.LoanStatus;
import com.aspire.lms.enums.RepaymentStatus;
import com.aspire.lms.model.Loan;
import com.aspire.lms.model.ScheduledRepayment;
import com.aspire.lms.model.User;
import com.aspire.lms.repository.LoanRepository;
import com.aspire.lms.repository.ScheduledRepaymentsRepository;
import com.aspire.lms.repository.UserRepository;
import com.aspire.lms.service.impl.LoanServiceImpl;
import com.aspire.lms.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class LoanServiceTest {

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
        when(userRepository.findByUserId(anyString())).thenAnswer((invocation) -> {
            String userId = invocation.getArgument(0);
            User user = new User();
            user.setUserId(userId);
            user.setEmail("test@example.com");
            user.setName("test");
            user.setAddress("123 Main Street");
            return Optional.of(user);
        });
    }

    @Test
    public void testCreateLoan_InvalidUserId() {
        // Mock userRepository to return an empty Optional, simulating user not found
        // Create a LoanRequest object with an invalid user ID
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setUserId("9999"); // Assuming an invalid user ID
        // Call the createLoan function and expect a RuntimeException to be thrown
        assertThrows(RuntimeException.class, () -> loanService.createLoan(loanRequest));
    }


    @Test
    public void testCreateLoan_Success() {

        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setUserId("test");
        loanRequest.setLoanTerm(10);
        loanRequest.setAmountRequired(1000.0);

        // Call the createLoan function
        Loan loan = loanService.createLoan(loanRequest);

        // Assertions
        assertNotNull(loan);
        assertNotNull(loan.getLoanId());
        assertEquals(LoanStatus.PENDING, loan.getStatus());
        assertEquals(loanRequest.getUserId(), loan.getUser().getUserId());
        assertEquals(loanRequest.getAmountRequired(), loan.getAmountRequired());
        assertEquals(loanRequest.getLoanTerm(), loan.getLoanTerm());
        assertEquals(loanRequest.getLoanTerm(), loan.getScheduledRepayments().size());

        // Check the first scheduled repayment
        ScheduledRepayment firstRepayment = loan.getScheduledRepayments().get(0);
        assertNotNull(firstRepayment);
        assertNotNull(firstRepayment.getRepaymentId());
        assertEquals(RepaymentStatus.PENDING, firstRepayment.getStatus());
        assertEquals(LocalDate.now().plusWeeks(1), firstRepayment.getDate());
    }

    @Test
    public void testCreateLoan_ScheduledRepayments() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setUserId("test");
        loanRequest.setLoanTerm(5);
        loanRequest.setAmountRequired(2000.0);

        // Call the createLoan function
        Loan loan = loanService.createLoan(loanRequest);

        // Assertions
        List<ScheduledRepayment> scheduledRepayments = loan.getScheduledRepayments();
        assertNotNull(scheduledRepayments);
        assertEquals(loanRequest.getLoanTerm(), scheduledRepayments.size());
        double totalRepayment = 0;
        for (ScheduledRepayment repayment : scheduledRepayments) {
            totalRepayment += repayment.getAmount();
            assertEquals(RepaymentStatus.PENDING, repayment.getStatus());
        }
        assertEquals(loanRequest.getAmountRequired(), totalRepayment, 0.001);
    }


}
