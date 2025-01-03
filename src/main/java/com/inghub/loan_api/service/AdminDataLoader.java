package com.inghub.loan_api.service;

import com.inghub.loan_api.models.entities.CustomerEntity;
import com.inghub.loan_api.models.entities.UserEntity;
import com.inghub.loan_api.models.enums.UserRole;
import com.inghub.loan_api.repository.CustomerRepository;
import com.inghub.loan_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataLoader(UserRepository userRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.findByTckn("111").isEmpty()) {
            UserEntity admin = UserEntity.builder()
                    .name("ADMIN")
                    .password(passwordEncoder.encode("admin"))
                    .tckn("111")
                    .role(UserRole.ADMIN)
                    .isActive(true)
                    .build();

            userRepository.save(admin);
        }
    }
}

