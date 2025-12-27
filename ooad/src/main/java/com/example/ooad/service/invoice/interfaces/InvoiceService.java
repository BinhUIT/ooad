package com.example.ooad.service.invoice.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.dto.request.invoice.InvoiceMedicineDetailRequest;
import com.example.ooad.dto.request.invoice.InvoiceSearchRequest;
import com.example.ooad.dto.request.invoice.InvoiceServiceDetailRequest;
import com.example.ooad.dto.request.invoice.UpdateInvoiceRequest;
import com.example.ooad.dto.response.invoice.AvailableMedicineResponse;
import com.example.ooad.dto.response.invoice.InvoiceDetailResponse;
import com.example.ooad.dto.response.invoice.InvoiceListResponse;

public interface InvoiceService {
    public Invoice findInvoiceById(int invoiceId);
    
    // Search invoices with pagination
    Page<InvoiceListResponse> searchInvoices(InvoiceSearchRequest request);
    
    // Get invoice detail with all tabs data
    InvoiceDetailResponse getInvoiceDetail(int invoiceId);
    
    // Update invoice payment method
    Invoice updateInvoice(int invoiceId, UpdateInvoiceRequest request);
    
    // Update invoice medicine details (add/update/delete)
    InvoiceDetailResponse updateMedicineDetails(int invoiceId, List<InvoiceMedicineDetailRequest> details);
    
    // Add a single medicine detail (without deleting existing ones)
    InvoiceDetailResponse addMedicineDetail(int invoiceId, InvoiceMedicineDetailRequest detail);
    
    // Update invoice service details (add/update/delete)
    InvoiceDetailResponse updateServiceDetails(int invoiceId, List<InvoiceServiceDetailRequest> details);
    
    // Add a single service detail (without deleting existing ones)
    InvoiceDetailResponse addServiceDetail(int invoiceId, InvoiceServiceDetailRequest detail);
    
    // Delete medicine detail
    void deleteMedicineDetail(int invoiceId, int detailId);
    
    // Delete service detail
    void deleteServiceDetail(int invoiceId, int detailId);
    
    // Get available medicines for sale (with inventory validation)
    List<AvailableMedicineResponse> getAvailableMedicines(int minMonthsBeforeExpiry);
    
    // Mark invoice as paid
    Invoice markAsPaid(int invoiceId, int paymentMethodId, int staffId);
    
    // Recalculate invoice totals
    void recalculateInvoiceTotals(int invoiceId);
    
    // Patient-specific methods
    Page<InvoiceListResponse> getPatientInvoices(int patientId, int page, int size);
    InvoiceDetailResponse getPatientInvoiceDetail(int patientId, int invoiceId);
}
