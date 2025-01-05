package com.inghub.loan_api;

import com.inghub.loan_api.exception.ProblemDetailsException;
import com.inghub.loan_api.models.entity.CustomerEntity;
import com.inghub.loan_api.models.entity.LoanEntity;
import com.inghub.loan_api.models.entity.LoanInstallmentEntity;
import com.inghub.loan_api.models.enums.NumberOfInstallments;
import com.inghub.loan_api.models.request.loan.CreateLoanRequest;
import com.inghub.loan_api.models.request.loan.LoanPaymentRequest;
import com.inghub.loan_api.models.response.loan.LoanPaymentResponse;
import com.inghub.loan_api.repository.LoanInstallmentRepository;
import com.inghub.loan_api.repository.LoanRepository;
import com.inghub.loan_api.service.CustomerService;
import com.inghub.loan_api.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLoan_ShouldCreateLoan_WhenValidRequest() {
        // Arrange
        CreateLoanRequest request = new CreateLoanRequest();
        request.setCustomerId(1L);
        request.setLoanAmount(BigDecimal.valueOf(10000));
        request.setInterestRate(0.1);
        request.setInstallmentNumber(NumberOfInstallments.TWELVE);

        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setCreditLimit(BigDecimal.valueOf(20000));
        customer.setUsedCreditLimit(BigDecimal.valueOf(5000));

        when(customerService.getById(1L)).thenReturn(Optional.of(customer));

        // Act
        loanService.createLoan(request);

        // Assert
        verify(loanRepository, times(1)).save(any(LoanEntity.class));
        verify(loanInstallmentRepository, times(1)).saveAll(anyList());
        verify(customerService, times(1)).updateCustomerUsedCreditLimit(eq(customer), eq(request.getLoanAmount()));
    }

    @Test
    void createLoan_ShouldThrowException_WhenCreditLimitExceeded() {
        // Arrange
        CreateLoanRequest request = new CreateLoanRequest();
        request.setCustomerId(1L);
        request.setLoanAmount(BigDecimal.valueOf(30000));

        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setCreditLimit(BigDecimal.valueOf(20000));
        customer.setUsedCreditLimit(BigDecimal.valueOf(5000));

        when(customerService.getById(1L)).thenReturn(Optional.of(customer));

        // Act & Assert
        ProblemDetailsException exception = assertThrows(ProblemDetailsException.class, () -> loanService.createLoan(request));
        assertEquals("Credit Limit Exceeded", exception.getProblemDetail().getTitle());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void payLoanInstallments_ShouldPayInstallments_WhenValidRequest() {
        // Arrange
        LoanPaymentRequest request = new LoanPaymentRequest();
        request.setLoanId(1L);
        request.setCustomerId(1L);
        request.setPaymentAmount(BigDecimal.valueOf(5000));

        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setLoanAmount(BigDecimal.valueOf(10000));
        loan.setIsPaid(false);

        LoanInstallmentEntity installment1 = new LoanInstallmentEntity();
        installment1.setAmount(BigDecimal.valueOf(3000));
        installment1.setIsPaid(false);
        installment1.setDueDate(LocalDate.now().minusDays(10));

        LoanInstallmentEntity installment2 = new LoanInstallmentEntity();
        installment2.setAmount(BigDecimal.valueOf(3000));
        installment2.setIsPaid(false);
        installment2.setDueDate(LocalDate.now().plusDays(10));

        List<LoanInstallmentEntity> installments = List.of(installment1, installment2);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanInstallmentRepository.findUnpaidInstallmentsByLoanId(1L)).thenReturn(installments);

        // Act
        LoanPaymentResponse response = loanService.payLoanInstallments(request);

        // Assert
        assertNotNull(response);

        assertEquals(
                BigDecimal.valueOf(3030.000).setScale(3, RoundingMode.HALF_UP),
                response.getTotalSpent().setScale(3, RoundingMode.HALF_UP)
        );
        assertEquals(
                BigDecimal.valueOf(1970).setScale(2, RoundingMode.HALF_UP),
                response.getRemainingBalance().setScale(2, RoundingMode.HALF_UP)
        );
    }

    @Test
    void payLoanInstallments_ShouldThrowException_WhenNoUnpaidInstallments() {
        // Arrange
        LoanPaymentRequest request = new LoanPaymentRequest();
        request.setLoanId(1L);
        request.setCustomerId(1L);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(new LoanEntity()));
        when(loanInstallmentRepository.findUnpaidInstallmentsByLoanId(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        ProblemDetailsException exception = assertThrows(ProblemDetailsException.class,
                () -> loanService.payLoanInstallments(request));
        assertEquals("Loan Installments Not Found", exception.getProblemDetail().getTitle());
    }
}
