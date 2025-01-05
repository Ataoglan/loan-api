package com.inghub.loan_api;

import com.inghub.loan_api.models.entity.LoanInstallmentEntity;
import com.inghub.loan_api.models.response.loaninstallment.GetLoanInstallmentResponse;
import com.inghub.loan_api.repository.LoanInstallmentRepository;
import com.inghub.loan_api.service.LoanInstallmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoanInstallmentServiceTest {

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @InjectMocks
    private LoanInstallmentService loanInstallmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByLoan_ShouldReturnInstallments_WhenValidLoanAndCustomerIds() {
        // Arrange
        Long loanId = 1L;
        Long customerId = 1L;

        LoanInstallmentEntity installment1 = new LoanInstallmentEntity();
        installment1.setAmount(BigDecimal.valueOf(1000));
        installment1.setPaidAmount(BigDecimal.valueOf(500));
        installment1.setDueDate(LocalDate.now().plusDays(30));
        installment1.setPaymentDate(LocalDate.now());
        installment1.setIsPaid(false);

        LoanInstallmentEntity installment2 = new LoanInstallmentEntity();
        installment2.setAmount(BigDecimal.valueOf(2000));
        installment2.setPaidAmount(BigDecimal.valueOf(1500));
        installment2.setDueDate(LocalDate.now().plusDays(60));
        installment2.setPaymentDate(LocalDate.now().plusDays(10));
        installment2.setIsPaid(false);

        List<LoanInstallmentEntity> mockInstallments = List.of(installment1, installment2);

        when(loanInstallmentRepository.findByLoanIdAndCustomerId(loanId, customerId))
                .thenReturn(mockInstallments);

        // Act
        List<GetLoanInstallmentResponse> response = loanInstallmentService.getByLoan(loanId, customerId);

        // Assert
        assertEquals(2, response.size());
        assertEquals(BigDecimal.valueOf(1000), response.get(0).getAmount());
        assertEquals(BigDecimal.valueOf(500), response.get(0).getPaidAmount());
        assertEquals(LocalDate.now().plusDays(30), response.get(0).getDueDate());
        assertEquals(LocalDate.now(), response.get(0).getPaymentDate());
        assertEquals(false, response.get(0).getIsPaid());

        verify(loanInstallmentRepository, times(1)).findByLoanIdAndCustomerId(loanId, customerId);
    }

    @Test
    void getByLoan_ShouldReturnEmptyList_WhenNoInstallmentsFound() {
        // Arrange
        Long loanId = 2L;
        Long customerId = 2L;

        when(loanInstallmentRepository.findByLoanIdAndCustomerId(loanId, customerId))
                .thenReturn(List.of());

        // Act
        List<GetLoanInstallmentResponse> response = loanInstallmentService.getByLoan(loanId, customerId);

        // Assert
        assertEquals(0, response.size());
        verify(loanInstallmentRepository, times(1)).findByLoanIdAndCustomerId(loanId, customerId);
    }
}
