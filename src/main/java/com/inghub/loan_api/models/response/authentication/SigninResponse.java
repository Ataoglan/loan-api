package com.inghub.loan_api.models.response.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninResponse {
    private String token;
}
