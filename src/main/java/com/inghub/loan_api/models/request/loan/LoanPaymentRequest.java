package com.inghub.loan_api.models.request.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanPaymentRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Loan ID cannot be null")
    private Long loanId;

    @NotNull(message = "Payment amount cannot be null")
    @Min(value = 1, message = "Payment amount must be at least 1")
    private BigDecimal paymentAmount;
}
