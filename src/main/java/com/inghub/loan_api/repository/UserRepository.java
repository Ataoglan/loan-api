package com.inghub.loan_api.repository;

import com.inghub.loan_api.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByTckn(String tckn);
}
