package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.InvoiceDetail;
import com.example.ooad.domain.entity.InvoiceServiceDetail;

@Repository
public interface InvoiceServiceDetailRepository extends JpaRepository<InvoiceServiceDetail, Integer> {
    public List<InvoiceServiceDetail> findByInvoice_InvoiceId(int invoiceId);
    public List<InvoiceServiceDetail> findByService_ServiceId(int serviceId);
    
}
