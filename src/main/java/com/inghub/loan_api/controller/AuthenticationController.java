package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.request.authentication.SigninRequest;
import com.inghub.loan_api.models.request.authentication.SignupRequest;
import com.inghub.loan_api.models.response.common.ApiResponse;
import com.inghub.loan_api.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationServic) {
        this.authenticationService = authenticationServic;
    };

    @PostMapping("signup")
    public ApiResponse signup(@Valid @RequestBody SignupRequest request) {
        authenticationService.signup(request);
        return ApiResponse.success("Signup successful. Welcome!");
    }

    @PostMapping("signin")
    public ApiResponse signin(@Valid @RequestBody SigninRequest request) {
        return ApiResponse.success(authenticationService.signin(request));
    }
}
