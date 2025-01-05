package com.inghub.loan_api.service;

import com.inghub.loan_api.models.entity.CustomerEntity;
import com.inghub.loan_api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void updateCustomerLimit(Long customerId, BigDecimal updateLimit) {
        getById(customerId)
                .ifPresent(customerEntity -> {
                    BigDecimal creditLimit = customerEntity.getCreditLimit();
                    creditLimit = creditLimit.add(updateLimit);

                    customerEntity.setCreditLimit(creditLimit);
                    customerRepository.save(customerEntity);
                });
    }

    public void updateCustomerUsedCreditLimit(CustomerEntity customer, BigDecimal loanAmount) {
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(loanAmount));
        customer.setCreditLimit(customer.getCreditLimit().subtract(loanAmount));
        customerRepository.save(customer);
    }

    public Optional<CustomerEntity> getById(Long customerId) {
        return customerRepository.findById(customerId);
    }
}
