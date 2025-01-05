package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.request.loan.CreateLoanRequest;
import com.inghub.loan_api.models.request.loan.LoanPaymentRequest;
import com.inghub.loan_api.models.response.common.ApiResponse;
import com.inghub.loan_api.service.LoanService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loans/")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and #request.customerId == principal.id)")
    public ApiResponse create(@RequestBody CreateLoanRequest request) {
        return ApiResponse.success(loanService.createLoan(request));
    }

    @PostMapping("/pay")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and #request.customerId == principal.id)")
    public ApiResponse pay(@RequestBody LoanPaymentRequest request) {
        return ApiResponse.success(loanService.payLoanInstallments(request));
    }
}
