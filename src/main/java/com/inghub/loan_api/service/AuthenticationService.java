package com.inghub.loan_api.service;

import com.inghub.loan_api.exception.ProblemDetailsException;
import com.inghub.loan_api.models.CustomUserDetails;
import com.inghub.loan_api.models.entity.CustomerEntity;
import com.inghub.loan_api.models.entity.UserEntity;
import com.inghub.loan_api.models.enums.UserRole;
import com.inghub.loan_api.models.request.authentication.SigninRequest;
import com.inghub.loan_api.models.request.authentication.SignupRequest;
import com.inghub.loan_api.models.response.authentication.SigninResponse;
import com.inghub.loan_api.repository.UserRepository;
import com.inghub.loan_api.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void signup(SignupRequest request) {
        userRepository.findByTckn(request.getTckn())
                .ifPresent(userEntity -> {
                    ProblemDetail problemDetail = ProblemDetail
                            .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                                    "User found with TCKN: " + request.getTckn());
                    problemDetail.setTitle("User Already Exists");

                    throw new ProblemDetailsException(problemDetail);
                });

        UserEntity user = UserEntity.builder()
                .tckn(request.getTckn())
                .name(request.getName() + " " + request.getSurname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CUSTOMER)
                .isActive(true)
                .build();

        CustomerEntity customer = new CustomerEntity();
        customer.setName(request.getName());
        customer.setSurname(request.getSurname());
        customer.setUser(user);
        user.setCustomer(customer);

        userRepository.save(user);
    }

    public SigninResponse signin(SigninRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getTckn(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(userDetails.getTckn(), userDetails.getAuthorities().toString(),
                userDetails.getId());

        return SigninResponse.builder()
                .token(token)
                .build();
    }
}
