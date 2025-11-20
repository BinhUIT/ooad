package com.example.ooad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ref_payment_method")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefPaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_method_id")
    private int paymentMethodId;
    @Column(name="method_code",unique=true, nullable=false, columnDefinition="VARCHAR(50)")
    protected String methodCode;
    @Column(name="method_name", nullable=false, columnDefinition="VARCHAR(100)")
    private String methodName;
    @Column(name="description", columnDefinition="VARCHAR(255)")
    protected String description;

   

    @Column(name="is_active",columnDefinition="TINYINT(1)")
    protected boolean isActive=false;

    @Column(name="sort_order")
    protected int sortOrder=0;
}
