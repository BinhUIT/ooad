package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Invoice;
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    public List<Invoice> findByPatient_PatientId(int patientId);
}
