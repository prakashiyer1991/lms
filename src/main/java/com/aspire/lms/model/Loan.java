package com.aspire.lms.model;

import com.aspire.lms.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`loan`")
public class Loan extends BaseEntity{


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(
            name = "loan_id"
    )
    private String loanId;

    @Column(
            name = "amount_required"
    )
    private double amountRequired;

    @Column(
            name = "loan_term"
    )
    private int loanTerm;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduledRepayment> scheduledRepayments;

    @Enumerated
    private LoanStatus status;
}
