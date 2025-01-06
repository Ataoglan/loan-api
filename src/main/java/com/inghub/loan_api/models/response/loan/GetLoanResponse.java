package com.inghub.loan_api.models.response.loan;

import com.inghub.loan_api.models.enums.NumberOfInstallments;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GetLoanResponse {
    private Long customerId;
    private Long loanId;
    private BigDecimal loanAmount;
    private int numberOfInstallment;
    private Boolean isPaid;
}
