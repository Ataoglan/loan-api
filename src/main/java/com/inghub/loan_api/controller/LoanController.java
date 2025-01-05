package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.enums.NumberOfInstallments;
import com.inghub.loan_api.models.request.loan.CreateLoanRequest;
import com.inghub.loan_api.models.request.loan.LoanPaymentRequest;
import com.inghub.loan_api.models.response.common.ApiResponse;
import com.inghub.loan_api.service.LoanService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans/")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and #request.customerId == principal.id)")
    public ApiResponse create(@Valid @RequestBody CreateLoanRequest request) {
        return ApiResponse.success(loanService.createLoan(request));
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and #request.customerId == principal.id)")
    public ApiResponse pay(@Valid @RequestBody LoanPaymentRequest request) {
        return ApiResponse.success(loanService.payLoanInstallments(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and #customerId == principal.id)")
    public ApiResponse getLoans(
            @RequestParam @NotNull(message = "Customer ID cannot be null") Long customerId,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) NumberOfInstallments installmentNumber) {

        return ApiResponse.success(loanService.getLoans(customerId, isPaid, installmentNumber));
    }
}
