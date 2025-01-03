package com.inghub.loan_api.repository;

import com.inghub.loan_api.models.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
}
