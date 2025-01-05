package com.inghub.loan_api;

import com.inghub.loan_api.exception.ProblemDetailsException;
import com.inghub.loan_api.models.CustomUserDetails;
import com.inghub.loan_api.models.entity.UserEntity;
import com.inghub.loan_api.models.request.authentication.SigninRequest;
import com.inghub.loan_api.models.request.authentication.SignupRequest;
import com.inghub.loan_api.models.response.authentication.SigninResponse;
import com.inghub.loan_api.repository.UserRepository;
import com.inghub.loan_api.service.AuthenticationService;
import com.inghub.loan_api.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_ShouldCreateNewUser_WhenTcknNotExists() {
        // Arrange
        SignupRequest request = new SignupRequest();
        request.setTckn("12345678901");
        request.setName("John");
        request.setSurname("Doe");
        request.setPassword("password");
        request.setCreditLimit(BigDecimal.valueOf(10000));

        when(userRepository.findByTckn("12345678901")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Act
        authenticationService.signup(request);

        // Assert
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void signup_ShouldThrowException_WhenTcknExists() {
        // Arrange
        SignupRequest request = new SignupRequest();
        request.setTckn("12345678901");

        UserEntity existingUser = new UserEntity();
        existingUser.setTckn("12345678901");

        when(userRepository.findByTckn("12345678901")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        ProblemDetailsException exception = assertThrows(ProblemDetailsException.class,
                () -> authenticationService.signup(request));
        assertEquals("User Already Exists", exception.getProblemDetail().getTitle());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void signin_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        SigninRequest request = new SigninRequest();
        request.setTckn("12345678901");
        request.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = new CustomUserDetails(
                1L,
                "12345678901",
                "mockUsername",
                "encodedPassword",
                Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER"))
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken("12345678901", "[CUSTOMER]", 1L))
                .thenReturn("mocked-jwt-token");

        // Act
        SigninResponse response = authenticationService.signin(request);

        // Assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
    }

    @Test
    void signin_ShouldThrowException_WhenAuthenticationFails() {
        // Arrange
        SigninRequest request = new SigninRequest();
        request.setTckn("12345678901");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationService.signin(request));
        assertEquals("Authentication failed", exception.getMessage());
    }
}
