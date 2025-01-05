package com.inghub.loan_api.models.response.loan;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LoanPaymentResponse {
    private int installmentsPaid;
    private BigDecimal totalSpent;
    private BigDecimal remainingBalance;
    private boolean loanFullyPaid;
}
