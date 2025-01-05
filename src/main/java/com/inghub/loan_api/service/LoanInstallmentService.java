package com.inghub.loan_api.service;

import com.inghub.loan_api.models.entity.LoanInstallmentEntity;
import com.inghub.loan_api.models.response.loaninstallment.GetLoanInstallmentResponse;
import com.inghub.loan_api.repository.LoanInstallmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanInstallmentService {
    private final LoanInstallmentRepository loanInstallmentRepository;

    public LoanInstallmentService(LoanInstallmentRepository loanInstallmentRepository) {
        this.loanInstallmentRepository = loanInstallmentRepository;
    }

    public List<GetLoanInstallmentResponse> getByLoan(Long loanId, Long customerId) {
        List<LoanInstallmentEntity> installments = loanInstallmentRepository
                .findByLoanIdAndCustomerId(loanId, customerId);

        return installments.stream().map(installment -> GetLoanInstallmentResponse.builder()
                .amount(installment.getAmount())
                .paidAmount(installment.getPaidAmount())
                .dueDate(installment.getDueDate())
                .paymentDate(installment.getPaymentDate())
                .isPaid(installment.getIsPaid())
                .build()).toList();
    }
}
