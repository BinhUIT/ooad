package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.InvoiceDetail;
import com.example.ooad.domain.entity.InvoiceMedicineDetail;

@Repository
public interface InvoiceMedicineDetailRepository extends JpaRepository<InvoiceMedicineDetail, Integer> {
    public List<InvoiceMedicineDetail> findByInvoice_InvoiceId(int invoiceId);
    
}
