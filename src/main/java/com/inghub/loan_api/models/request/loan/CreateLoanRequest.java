package com.inghub.loan_api.models.request.loan;

import com.inghub.loan_api.models.enums.NumberOfInstallments;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateLoanRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Customer ID cannot be null")
    @Min(value = 1, message = "Loan amount must be at least 1")
    private BigDecimal loanAmount;

    @NotNull(message = "Installment number cannot be null")
    private int installmentNumber;

    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
    @DecimalMax(value = "0.5", message = "Interest rate must be at most 0.5")
    private Double interestRate;
}
