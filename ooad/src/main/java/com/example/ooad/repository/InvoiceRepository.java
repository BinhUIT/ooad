package com.example.ooad.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EPaymentStatus;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    public List<Invoice> findByPatient_PatientId(int patientId);
    
    public Optional<Invoice> findByRecord_RecordId(int recordId);
    
    @Query("SELECT i FROM Invoice i " +
           "WHERE (:patientName IS NULL OR LOWER(i.patient.fullName) LIKE LOWER(CONCAT('%', :patientName, '%'))) " +
           "AND (:patientPhone IS NULL OR i.patient.phone LIKE CONCAT('%', :patientPhone, '%')) " +
           "AND (:patientId IS NULL OR i.patient.patientId = :patientId) " +
           "AND (:recordId IS NULL OR i.record.recordId = :recordId) " +
           "AND (:paymentStatus IS NULL OR i.paymentStatus = :paymentStatus) " +
           "AND (:fromDate IS NULL OR i.invoiceDate >= :fromDate) " +
           "AND (:toDate IS NULL OR i.invoiceDate <= :toDate) " +
           "ORDER BY i.invoiceDate DESC, i.invoiceId DESC")
    Page<Invoice> searchInvoices(
            @Param("patientName") String patientName,
            @Param("patientPhone") String patientPhone,
            @Param("patientId") Integer patientId,
            @Param("recordId") Integer recordId,
            @Param("paymentStatus") EPaymentStatus paymentStatus,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            Pageable pageable);
    
    @Query("SELECT i FROM Invoice i WHERE i.patient.patientId = :patientId ORDER BY i.invoiceDate DESC, i.invoiceId DESC")
    Page<Invoice> findByPatientIdPaginated(@Param("patientId") int patientId, Pageable pageable);
    @Query("SELECT i FROM Invoice i WHERE month(i.invoiceDate)=:month and year(i.invoiceDate)=:year and i.paymentStatus=:status")
    List<Invoice> findByInvoiceDateAndPaymentStatus(@Param("month") int month, @Param("year") int year,@Param("status") EPaymentStatus status);

    List<Invoice> findByPatient_PatientIdAndPaymentStatus(int patientId, EPaymentStatus status);
    
    // Revenue statistics queries
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate = :date AND i.paymentStatus = :status")
    List<Invoice> findByInvoiceDateAndStatus(@Param("date") Date date, @Param("status") EPaymentStatus status);
    
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate = :date AND i.paymentStatus = :status AND i.record.doctor = :doctor")
    List<Invoice> findByInvoiceDateAndStatusAndDoctor(@Param("date") Date date, @Param("status") EPaymentStatus status, @Param("doctor") Staff doctor);
    
    @Query("SELECT i FROM Invoice i WHERE YEAR(i.invoiceDate) = :year AND i.paymentStatus = :status")
    List<Invoice> findByYearAndStatus(@Param("year") int year, @Param("status") EPaymentStatus status);
    
    @Query("SELECT i FROM Invoice i WHERE YEAR(i.invoiceDate) = :year AND i.paymentStatus = :status AND i.record.doctor = :doctor")
    List<Invoice> findByYearAndStatusAndDoctor(@Param("year") int year, @Param("status") EPaymentStatus status, @Param("doctor") Staff doctor);
    
    @Query("SELECT i FROM Invoice i WHERE MONTH(i.invoiceDate) = :month AND YEAR(i.invoiceDate) = :year AND i.paymentStatus = :status AND i.record.doctor = :doctor")
    List<Invoice> findByMonthYearStatusAndDoctor(@Param("month") int month, @Param("year") int year, @Param("status") EPaymentStatus status, @Param("doctor") Staff doctor);
    
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :fromDate AND :toDate AND i.paymentStatus = :status")
    List<Invoice> findByDateRangeAndStatus(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("status") EPaymentStatus status);
    
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :fromDate AND :toDate AND i.paymentStatus = :status AND i.record.doctor = :doctor")
    List<Invoice> findByDateRangeAndStatusAndDoctor(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("status") EPaymentStatus status, @Param("doctor") Staff doctor);
}
