package com.inghub.loan_api.models.request.authentication;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAdminRequest {
    @NotNull(message = "TCKN cannot be null")
    @Pattern(regexp = "\\d{11}", message = "TCKN must be exactly 11 digits")
    private String tckn;

    @NotNull(message = "name cannot be null")
    private String name;

    @NotNull(message = "username cannot be null")
    private String surname;

    @NotNull(message = "password cannot be null")
    private String password;
}
