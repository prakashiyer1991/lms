package com.aspire.lms.repository;

import com.aspire.lms.model.ScheduledRepayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledRepaymentsRepository extends CrudRepository<ScheduledRepayment,Long> {
    List<ScheduledRepayment> findByLoanId(Long loanId);
}
