package com.inghub.loan_api.service;

import com.inghub.loan_api.exception.ProblemDetailsException;
import com.inghub.loan_api.models.entity.CustomerEntity;
import com.inghub.loan_api.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
                .ifPresentOrElse(
                        customerEntity -> {
                            customerEntity.setCreditLimit(updateLimit);
                            customerRepository.save(customerEntity);
                        },
                        () -> {
                            ProblemDetail problemDetail = ProblemDetail
                                    .forStatusAndDetail(HttpStatus.NOT_FOUND,
                                            "Customer not found with ID: " + customerId);
                            problemDetail.setTitle("Customer Not Found");
                            throw new ProblemDetailsException(problemDetail);
                        }
                );
    }

    public void recoverCustomerLimit(Long customerId, BigDecimal updateLimit) {
        getById(customerId)
                .ifPresent(customerEntity -> {
                    BigDecimal creditLimit = customerEntity.getUsedCreditLimit();
                    creditLimit = creditLimit.subtract(updateLimit);

                    customerEntity.setUsedCreditLimit(creditLimit);
                    customerRepository.save(customerEntity);
                });
    }

    public void updateCustomerUsedCreditLimit(CustomerEntity customer, BigDecimal loanAmount) {
        customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(loanAmount));
        customerRepository.save(customer);
    }

    public Optional<CustomerEntity> getById(Long customerId) {
        return customerRepository.findById(customerId);
    }
}
