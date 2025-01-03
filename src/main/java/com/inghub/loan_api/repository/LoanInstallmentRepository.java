package com.inghub.loan_api.repository;

import com.inghub.loan_api.models.entities.LoanInstallmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallmentEntity, Long> {
}
