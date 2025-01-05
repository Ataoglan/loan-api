package com.inghub.loan_api.service;

import com.inghub.loan_api.models.entity.UserEntity;
import com.inghub.loan_api.models.enums.UserRole;
import com.inghub.loan_api.models.request.authentication.CreateAdminRequest;
import com.inghub.loan_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(CreateAdminRequest request) {
        UserEntity user = UserEntity.builder()
                .tckn(request.getTckn())
                .name(request.getName() + " " + request.getSurname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();

        userRepository.save(user);
    }
}
