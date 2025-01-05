package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.request.authentication.CreateAdminRequest;
import com.inghub.loan_api.models.response.common.ApiResponse;
import com.inghub.loan_api.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse create(@Valid @RequestBody CreateAdminRequest request) {
        adminService.create(request);

        return ApiResponse.success();
    }
}
