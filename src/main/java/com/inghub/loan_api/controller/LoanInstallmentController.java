package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.response.common.ApiResponse;
import com.inghub.loan_api.service.LoanInstallmentService;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan-installments/")
public class LoanInstallmentController {
    private final LoanInstallmentService loanInstallmentService;

    public LoanInstallmentController(LoanInstallmentService loanInstallmentService) {
        this.loanInstallmentService = loanInstallmentService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and #customerId == principal.id)")
    public ApiResponse getLoanInstallments(
            @RequestParam @NotNull(message = "Loan ID cannot be null") Long loanId,
            @RequestParam @NotNull(message = "Customer ID cannot be null") Long customerId) {

        return ApiResponse.success(loanInstallmentService.getByLoan(loanId, customerId));
    }
}
