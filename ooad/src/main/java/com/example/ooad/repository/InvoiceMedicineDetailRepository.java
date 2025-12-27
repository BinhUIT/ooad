package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.InvoiceMedicineDetail;

@Repository
public interface InvoiceMedicineDetailRepository extends JpaRepository<InvoiceMedicineDetail, Integer> {
    public List<InvoiceMedicineDetail> findByInvoice_InvoiceId(int invoiceId);
    
    @Modifying
    @Query("DELETE FROM InvoiceMedicineDetail d WHERE d.invoice.invoiceId = :invoiceId")
    void deleteByInvoiceId(@Param("invoiceId") int invoiceId);
    
    @Modifying
    @Query("DELETE FROM InvoiceMedicineDetail d WHERE d.invoice.invoiceId = :invoiceId AND d.detailId NOT IN :keepIds")
    void deleteByInvoiceIdAndDetailIdNotIn(@Param("invoiceId") int invoiceId, @Param("keepIds") List<Integer> keepIds);
}
