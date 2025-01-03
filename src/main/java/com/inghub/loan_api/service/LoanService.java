package com.inghub.loan_api.service;

import com.inghub.loan_api.models.entities.CustomerEntity;
import com.inghub.loan_api.models.entities.LoanEntity;
import com.inghub.loan_api.models.entities.LoanInstallmentEntity;
import com.inghub.loan_api.models.enums.NumberOfInstallments;
import com.inghub.loan_api.models.enums.UserRole;
import com.inghub.loan_api.models.request.loan.CreateLoanRequest;
import com.inghub.loan_api.repository.CustomerRepository;
import com.inghub.loan_api.repository.LoanInstallmentRepository;
import com.inghub.loan_api.repository.LoanRepository;
import com.inghub.loan_api.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final JwtUtil jwtUtil;

    public LoanService(CustomerRepository customerRepository, LoanRepository loanRepository,
                       LoanInstallmentRepository loanInstallmentRepository, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public LoanEntity createLoan(CreateLoanRequest request) {
        CustomerEntity customer = validateUser(request.getCustomerId());

        validateCreditLimit(customer, request.getLoanAmount());

        validateInstallmentNumber(request.getInstallmentNumber());

        validateInterestRate(request.getInterestRate());

        Double totalAmount = request.getLoanAmount()*(request.getInterestRate()+1);
        LoanEntity loan = new LoanEntity();
        loan.setCustomer(customer);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setNumberOfInstallment(NumberOfInstallments.fromValue(request.getInstallmentNumber()));
        loan.setIsPaid(false);
        loanRepository.save(loan);

        createInstallments(loan, request.getInstallmentNumber(), totalAmount);

        updateCustomerCreditLimit(customer, request.getLoanAmount());

        return loan;
    }

    private CustomerEntity validateUser(Long customerId) {
        String role = jwtUtil.extractRoleFromToken();
        Long userId = jwtUtil.extractUserIdFromToken();
        if (UserRole.CUSTOMER.toString().equals(role) && !userId.equals(customerId)) {
            //todo throw exception
        }

        return customerRepository.findById(customerId)
                .orElseThrow(null);
    }

    private void validateCreditLimit(CustomerEntity customer, Double loanAmount) {
        double availableLimit = customer.getCreditLimit() - customer.getUsedCreditLimit();
        if (loanAmount > availableLimit) {
            //todo exception
        }
    }

    private void validateInstallmentNumber(int installmentNumber) {
        List<Integer> validInstallments = List.of(6, 9, 12, 24);
        if (!validInstallments.contains(installmentNumber)) {
            //todo exception
        }
    }

    private void validateInterestRate(Double interestRate) {
        if (interestRate < 0.1 || interestRate > 0.5) {
            //todo exception
        }
    }

    private void createInstallments(LoanEntity loan, int installmentNumber, Double totalAmount) {
        double installmentAmount = totalAmount / installmentNumber;
        List<LoanInstallmentEntity> installments = new ArrayList<>();
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        for (int i = 0; i < installmentNumber; i++) {
            LoanInstallmentEntity installment = new LoanInstallmentEntity();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setPaidAmount(0.0);
            installment.setDueDate(dueDate);
            installment.setIsPaid(false);
            installments.add(installment);
            dueDate = dueDate.plusMonths(1);
        }

        loanInstallmentRepository.saveAll(installments);
    }

    private void updateCustomerCreditLimit(CustomerEntity customer, Double loanAmount) {
        customer.setUsedCreditLimit(customer.getUsedCreditLimit() + loanAmount);
        customerRepository.save(customer);
    }
}
