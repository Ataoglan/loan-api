package com.inghub.loan_api.models.request.authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SigninRequest {
    @NotNull(message = "TCKN cannot be null")
    @Pattern(regexp = "\\d{11}", message = "TCKN must be exactly 11 digits")
    private String tckn;

    @NotNull(message = "password cannot be null")
    private String password;
}
