package com.inghub.loan_api.repository;

import com.inghub.loan_api.models.entity.LoanInstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallmentEntity, Long> {
    @Query("SELECT l FROM LoanInstallmentEntity l WHERE l.loan.id = :loanId AND l.isPaid = false ORDER BY l.dueDate ASC")
    List<LoanInstallmentEntity> findUnpaidInstallmentsByLoanId(Long loanId);

    @Query("SELECT i FROM LoanInstallmentEntity i " +
            "WHERE i.loan.id = :loanId AND i.loan.customer.id = :customerId")
    List<LoanInstallmentEntity> findByLoanIdAndCustomerId(@Param("loanId") Long loanId, @Param("customerId") Long customerId);
}
