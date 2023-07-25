package com.aspire.lms.repository;

import com.aspire.lms.model.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends CrudRepository<Loan,Long> {
    List<Loan> findByUserId(Long userId);

    Optional<Loan> findByLoanId(String loanId);
}
