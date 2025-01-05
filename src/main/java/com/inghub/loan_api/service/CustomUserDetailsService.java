package com.inghub.loan_api.service;


import com.inghub.loan_api.models.CustomUserDetails;
import com.inghub.loan_api.models.entity.UserEntity;
import com.inghub.loan_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String tckn) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByTckn(tckn)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + tckn));

        return CustomUserDetails.builder()
                .id(user.getId())
                .tckn(user.getTckn())
                .username(user.getName())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .build();
    }
}
