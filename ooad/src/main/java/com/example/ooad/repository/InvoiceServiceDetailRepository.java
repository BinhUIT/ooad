package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.InvoiceServiceDetail;

@Repository
public interface InvoiceServiceDetailRepository extends JpaRepository<InvoiceServiceDetail, Integer> {
    public List<InvoiceServiceDetail> findByInvoice_InvoiceId(int invoiceId);
    public List<InvoiceServiceDetail> findByService_ServiceId(int serviceId);
    
    @Modifying
    @Query("DELETE FROM InvoiceServiceDetail d WHERE d.invoice.invoiceId = :invoiceId")
    void deleteByInvoiceId(@Param("invoiceId") int invoiceId);
    
    @Modifying
    @Query("DELETE FROM InvoiceServiceDetail d WHERE d.invoice.invoiceId = :invoiceId AND d.detailId NOT IN :keepIds")
    void deleteByInvoiceIdAndDetailIdNotIn(@Param("invoiceId") int invoiceId, @Param("keepIds") List<Integer> keepIds);
}
