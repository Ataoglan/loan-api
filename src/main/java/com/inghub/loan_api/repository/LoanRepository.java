package com.inghub.loan_api.repository;

import com.inghub.loan_api.models.entity.LoanEntity;
import com.inghub.loan_api.models.enums.NumberOfInstallments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    @Query("SELECT l FROM LoanEntity l WHERE l.customer.id = :customerId " +
            "AND (:isPaid IS NULL OR l.isPaid = :isPaid) " +
            "AND (:installments IS NULL OR l.numberOfInstallment = :installments)")
    List<LoanEntity> findLoansByCustomerAndFilters(
            @Param("customerId") Long customerId,
            @Param("isPaid") Boolean isPaid,
            @Param("installments") NumberOfInstallments installments);
}
