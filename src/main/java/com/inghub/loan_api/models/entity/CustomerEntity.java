package com.inghub.loan_api.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class CustomerEntity {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private UserEntity user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "credit_limit", nullable = false)
    private BigDecimal creditLimit= BigDecimal.valueOf(0.0);

    @Column(name = "used_credit_limit", nullable = false)
    private BigDecimal usedCreditLimit = BigDecimal.valueOf(0.0);
}
