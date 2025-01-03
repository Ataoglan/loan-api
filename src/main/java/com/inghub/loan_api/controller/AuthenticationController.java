package com.inghub.loan_api.controller;

import com.inghub.loan_api.models.request.authentication.SignupRequest;
import com.inghub.loan_api.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/auth/")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationServic) {
        this.authenticationService = authenticationServic;
    };

    @PostMapping("signup")
    public void signup(@RequestBody SignupRequest request) {
        authenticationService.signup(request);
    }
}
