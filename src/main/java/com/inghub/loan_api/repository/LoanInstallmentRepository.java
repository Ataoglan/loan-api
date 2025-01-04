package com.inghub.loan_api.repository;

import com.inghub.loan_api.models.entities.LoanInstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallmentEntity, Long> {
    @Query("SELECT l FROM LoanInstallmentEntity l WHERE l.loan.id = :loanId AND l.isPaid = false ORDER BY l.dueDate ASC")
    List<LoanInstallmentEntity> findUnpaidInstallmentsByLoanId(Long loanId);
}
