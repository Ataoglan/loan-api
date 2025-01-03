package com.inghub.loan_api.service;

import com.inghub.loan_api.models.entities.CustomerEntity;
import com.inghub.loan_api.models.entities.UserEntity;
import com.inghub.loan_api.models.enums.UserRole;
import com.inghub.loan_api.models.request.authentication.SigninRequest;
import com.inghub.loan_api.models.request.authentication.SignupRequest;
import com.inghub.loan_api.models.response.authentication.SigninResponse;
import com.inghub.loan_api.repository.CustomerRepository;
import com.inghub.loan_api.repository.UserRepository;
import com.inghub.loan_api.utils.JwtAuthenticationFilter;
import com.inghub.loan_api.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.findByTckn(request.getTckn()).isPresent()) {
            //todo exception
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
            customer.setName(request.getName());
            customer.setSurname(request.getSurname());
            customer.setUser(user);
            customer.setCreditLimit(request.getCreditLimit());
            user.setCustomer(customer);
        }

        userRepository.save(user);
    }

    public SigninResponse signin(SigninRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByTckn(request.getTckn());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getTckn(), user.getRole().name(), request.getUserId());
                return SigninResponse.builder().token(token).build();
            }
        }

        return null; //todo exception
    }
}
