package com.inghub.loan_api.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_installments")
public class LoanInstallmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanEntity loan;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "paid_amount", nullable = false)
    private BigDecimal paidAmount = BigDecimal.valueOf(0.0);

    @Column(name = "due_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private LocalDate paymentDate;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;
}
