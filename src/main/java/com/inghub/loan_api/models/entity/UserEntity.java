package com.inghub.loan_api.models.entity;

import com.inghub.loan_api.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String tckn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, optional = true)
    private CustomerEntity customer;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}

