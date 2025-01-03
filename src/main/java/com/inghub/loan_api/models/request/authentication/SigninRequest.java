package com.inghub.loan_api.models.request.authentication;

import lombok.Data;

@Data
public class SigninRequest {
    private String tckn;
    private Long userId;
    private String password;
}
