package com.inghub.loan_api.models.request.loan;

import lombok.Data;

@Data
public class CreateLoanRequest {
    private Long customerId;
    private Double loanAmount;
    private int installmentNumber;
    private Double interestRate;
}
