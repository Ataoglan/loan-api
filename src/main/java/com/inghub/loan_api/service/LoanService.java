package com.inghub.loan_api.service;

import com.inghub.loan_api.exception.ProblemDetailsException;
import com.inghub.loan_api.models.entity.CustomerEntity;
import com.inghub.loan_api.models.entity.LoanEntity;
import com.inghub.loan_api.models.entity.LoanInstallmentEntity;
import com.inghub.loan_api.models.enums.NumberOfInstallments;
import com.inghub.loan_api.models.request.loan.CreateLoanRequest;
import com.inghub.loan_api.models.request.loan.LoanPaymentRequest;
import com.inghub.loan_api.models.response.loan.CreateLoanResponse;
import com.inghub.loan_api.models.response.loan.LoanPaymentResponse;
import com.inghub.loan_api.repository.LoanInstallmentRepository;
import com.inghub.loan_api.repository.LoanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository loanInstallmentRepository;

    public LoanService(CustomerService customerService, LoanRepository loanRepository,
                       LoanInstallmentRepository loanInstallmentRepository) {
        this.customerService = customerService;
        this.loanRepository = loanRepository;
        this.loanInstallmentRepository = loanInstallmentRepository;
    }

    @Transactional
    public CreateLoanResponse createLoan(CreateLoanRequest request) {

        CustomerEntity customer = customerService.getById(request.getCustomerId())
                .orElseThrow(() -> {
                    ProblemDetail problemDetail = ProblemDetail
                            .forStatusAndDetail(HttpStatus.NOT_FOUND,
                                    "Customer not found with ID: " + request.getCustomerId());
                    problemDetail.setTitle("Customer Not Found");

                    return new ProblemDetailsException(problemDetail);
                });

        validateCreditLimit(customer, request.getLoanAmount());

        BigDecimal totalAmount = request.getLoanAmount().multiply(BigDecimal.valueOf(request.getInterestRate()+1));

        LoanEntity loan = new LoanEntity();
        loan.setCustomer(customer);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setNumberOfInstallment(NumberOfInstallments.fromValue(request.getInstallmentNumber().getValue()));
        loan.setIsPaid(false);
        loanRepository.save(loan);

        createInstallments(loan, request.getInstallmentNumber().getValue(), totalAmount);

        customerService.updateCustomerUsedCreditLimit(customer, request.getLoanAmount());

        return CreateLoanResponse.builder()
                .customerId(customer.getId())
                .numberOfInstallment(request.getInstallmentNumber())
                .isPaid(false)
                .loanAmount(loan.getLoanAmount())
                .build();
    }

    @Transactional
    public LoanPaymentResponse payLoanInstallments(LoanPaymentRequest request) {
        LoanEntity loan = loanRepository.findById(request.getLoanId())
                .orElseThrow(() -> {
                    ProblemDetail problemDetail = ProblemDetail
                            .forStatusAndDetail(HttpStatus.NOT_FOUND,
                                    "Loan not found with ID: " + request.getLoanId());
                    problemDetail.setTitle("Loan Not Found");

                    return new ProblemDetailsException(problemDetail);
                });

        List<LoanInstallmentEntity> installments = loanInstallmentRepository
                .findUnpaidInstallmentsByLoanId(request.getLoanId());

        if (installments.isEmpty()) {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.NOT_FOUND,
                            "No unpaid installments available for this loan. Loan ID: " +
                                    request.getLoanId());

            problemDetail.setTitle("Loan Installments Not Found");

            throw new ProblemDetailsException(problemDetail);
        }

        BigDecimal remainingPayment = request.getPaymentAmount();
        int installmentsPaid = 0;
        BigDecimal totalSpent = BigDecimal.valueOf(0);

        LoanPaymentResponse loanPaymentResponse =
                loanInstallmentsPayment(installments, remainingPayment, totalSpent, installmentsPaid);

        if (installments.stream().allMatch(LoanInstallmentEntity::getIsPaid)) {
            loan.setIsPaid(true);
            loanRepository.save(loan);
            customerService.updateCustomerLimit(request.getCustomerId(), loan.getLoanAmount());
        }

        loanPaymentResponse.setLoanFullyPaid(loan.getIsPaid());

        return loanPaymentResponse;
    }

    private LoanPaymentResponse loanInstallmentsPayment(List<LoanInstallmentEntity> installments,
                                         BigDecimal remainingPayment, BigDecimal totalSpent, int installmentsPaid) {

        for (LoanInstallmentEntity installment : installments) {
            if (isBeyondThreeMonths(installment.getDueDate())) {
                continue;
            }

            BigDecimal installmentAmount = calculateAdjustedInstallmentAmount(installment);
            if (remainingPayment.compareTo(installmentAmount) >= 0) {
                remainingPayment = remainingPayment.subtract(installmentAmount);
                totalSpent = totalSpent.add(installmentAmount);

                installment.setIsPaid(true);
                installment.setPaidAmount(installmentAmount);
                installment.setPaymentDate(LocalDate.now());
                loanInstallmentRepository.save(installment);

                installmentsPaid++;
            } else {
                break;
            }
        }

        return new LoanPaymentResponse(installmentsPaid, totalSpent, remainingPayment, false);
    }

    private BigDecimal calculateAdjustedInstallmentAmount(LoanInstallmentEntity installment) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(installment.getDueDate())) {
            long daysBefore = ChronoUnit.DAYS.between(today, installment.getDueDate());
            BigDecimal discount = installment.getAmount()
                    .multiply(BigDecimal.valueOf(0.001))
                    .multiply(BigDecimal.valueOf(daysBefore));

            return installment.getAmount().subtract(discount);
        } else if (today.isAfter(installment.getDueDate())) {
            long daysAfter = ChronoUnit.DAYS.between(installment.getDueDate(), today);
            BigDecimal penalty = installment.getAmount()
                    .multiply(BigDecimal.valueOf(0.001))
                    .multiply(BigDecimal.valueOf(daysAfter));

            return installment.getAmount().add(penalty);
        } else {
            return installment.getAmount();
        }
    }

    private boolean isBeyondThreeMonths(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        LocalDate maxPaymentDate = today.plusMonths(3);
        return dueDate.isAfter(maxPaymentDate);
    }

    private void validateCreditLimit(CustomerEntity customer, BigDecimal loanAmount) {
        BigDecimal availableLimit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());

        if (loanAmount.compareTo(availableLimit) > 0) {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                            "Loan amount exceeds available credit limit.");
            problemDetail.setTitle("Credit Limit Exceeded");
            problemDetail.setInstance(URI.create("/loans/validate-credit-limit"));
            problemDetail.setProperty("availableLimit", availableLimit);
            problemDetail.setProperty("requestedAmount", loanAmount);

            throw new ProblemDetailsException(problemDetail);
        }
    }

    private void createInstallments(LoanEntity loan, int installmentNumber, BigDecimal totalAmount) {
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(installmentNumber),
                2, RoundingMode.HALF_UP);

        List<LoanInstallmentEntity> installments = new ArrayList<>();
        LocalDate dueDate = LocalDate.now().plusMonths(1).withDayOfMonth(1);

        for (int i = 0; i < installmentNumber; i++) {
            LoanInstallmentEntity installment = new LoanInstallmentEntity();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setPaidAmount(BigDecimal.valueOf(0.0));
            installment.setDueDate(dueDate);
            installment.setIsPaid(false);
            installments.add(installment);
            dueDate = dueDate.plusMonths(1);
        }

        loanInstallmentRepository.saveAll(installments);
    }
}
