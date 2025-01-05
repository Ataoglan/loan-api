package com.inghub.loan_api.models.entity;

import com.inghub.loan_api.models.enums.NumberOfInstallments;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loans")
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "credit_limit")
    private BigDecimal loanAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "installment_number")
    private NumberOfInstallments numberOfInstallment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "is_paid")
    private Boolean isPaid;
}
