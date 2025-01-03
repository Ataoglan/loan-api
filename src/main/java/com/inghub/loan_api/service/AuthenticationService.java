package com.inghub.loan_api.service;

import com.inghub.loan_api.models.entities.CustomerEntity;
import com.inghub.loan_api.models.entities.UserEntity;
import com.inghub.loan_api.models.enums.UserRole;
import com.inghub.loan_api.models.request.authentication.SignupRequest;
import com.inghub.loan_api.repository.CustomerRepository;
import com.inghub.loan_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(SignupRequest request) {
        if (userRepository.findByTckn(request.getTckn()).isPresent()) {
            // return ResponseEntity.badRequest().body("TCKN already exists");
        }

        UserEntity user = UserEntity.builder()
                .tckn(request.getTckn())
                .name(request.getName() + " " + request.getSurname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        if (request.getRole().equals(UserRole.CUSTOMER)) {
            CustomerEntity customer = new CustomerEntity();
            customer.setUser(user);
            customer.setCreditLimit(request.getCreditLimit());
            user.setCustomer(customer);
        }

        userRepository.save(user);
    }
}
