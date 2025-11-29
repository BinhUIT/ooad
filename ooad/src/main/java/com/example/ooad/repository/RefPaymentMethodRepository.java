package com.example.ooad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.RefPaymentMethod;

@Repository
public interface RefPaymentMethodRepository extends JpaRepository<RefPaymentMethod, Integer> {
    Optional<RefPaymentMethod> findByMethodCode(String methodCode);
    boolean existsByMethodCode(String methodCode);
}
