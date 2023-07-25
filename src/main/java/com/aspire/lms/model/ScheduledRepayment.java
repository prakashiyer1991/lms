package com.aspire.lms.model;

import com.aspire.lms.enums.RepaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`scheduled_repayment`")
public class ScheduledRepayment extends BaseEntity {

    @Column(
            name = "repayment_id"
    )
    private String repaymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    private Double amount;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private RepaymentStatus status;
}
