package com.inghub.loan_api.models.request.authentication;

import com.inghub.loan_api.models.enums.UserRole;
import lombok.Data;

@Data
public class SignupRequest {
    private String tckn;
    private String name;
    private String surname;
    private String password;
    private UserRole role;
    private Double creditLimit;
}
