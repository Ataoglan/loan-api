package com.inghub.loan_api.models.response.loan;

import com.inghub.loan_api.models.enums.NumberOfInstallments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class CreateLoanResponse {

    private Long customerId;
    private BigDecimal loanAmount;
    private NumberOfInstallments numberOfInstallment;
    private Date createdAt;
    private Boolean isPaid;
}
