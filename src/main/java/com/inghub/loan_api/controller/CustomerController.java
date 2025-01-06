package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.response.common.ApiResponse;
import com.inghub.loan_api.service.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/customer/")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PatchMapping("update-credit-limit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse updateCustomerLimit(
            @RequestParam Long customerId,
            @RequestParam BigDecimal creditLimit) {

        customerService.updateCustomerLimit(customerId, creditLimit);
        return ApiResponse.success("Credit limit updated successfully.");
    }

}
