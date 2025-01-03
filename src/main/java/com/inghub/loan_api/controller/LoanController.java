package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.request.loan.CreateLoanRequest;
import com.inghub.loan_api.models.response.common.ApiResponse;
import com.inghub.loan_api.service.LoanService;
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

    @RequestMapping("create")
    public ApiResponse create(@RequestBody CreateLoanRequest request) {
        loanService.createLoan(request);
        return ApiResponse.success();
    }
}
