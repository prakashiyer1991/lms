package com.aspire.lms.model;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@Table(name = "`user`")
public class User extends BaseEntity{

    @Column(
            name = "user_id"
    )
    private String userId;

    private String name;

    private String email;

    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loanList;
}
