package com.inghub.loan_api;

import com.inghub.loan_api.models.entity.CustomerEntity;
import com.inghub.loan_api.repository.CustomerRepository;
import com.inghub.loan_api.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateCustomerLimit_ShouldUpdateLimit_WhenCustomerExists() {
        Long customerId = 1L;
        BigDecimal updateLimit = BigDecimal.valueOf(500);
        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        customer.setCreditLimit(BigDecimal.valueOf(1000));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        customerService.recoverCustomerLimit(customerId, updateLimit);

        assertEquals(BigDecimal.valueOf(1500), customer.getCreditLimit());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateCustomerLimit_ShouldDoNothing_WhenCustomerDoesNotExist() {
        Long customerId = 1L;
        BigDecimal updateLimit = BigDecimal.valueOf(500);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        customerService.recoverCustomerLimit(customerId, updateLimit);

        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateCustomerUsedCreditLimit_ShouldUpdateLimitsCorrectly() {
        CustomerEntity customer = new CustomerEntity();
        customer.setUsedCreditLimit(BigDecimal.valueOf(500));
        customer.setCreditLimit(BigDecimal.valueOf(1500));
        BigDecimal loanAmount = BigDecimal.valueOf(200);

        customerService.updateCustomerUsedCreditLimit(customer, loanAmount);

        assertEquals(BigDecimal.valueOf(700), customer.getUsedCreditLimit());
        assertEquals(BigDecimal.valueOf(1500), customer.getCreditLimit());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void getById_ShouldReturnCustomer_WhenCustomerExists() {
        Long customerId = 1L;
        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Optional<CustomerEntity> result = customerService.getById(customerId);

        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().getId());
    }

    @Test
    void getById_ShouldReturnEmpty_WhenCustomerDoesNotExist() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerEntity> result = customerService.getById(customerId);

        assertFalse(result.isPresent());
    }
}
