package com.inghub.loan_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String tckn;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
}
