package com.example.ooad.data_refactor;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.InvoiceDetail;
import com.example.ooad.domain.entity.InvoiceMedicineDetail;
import com.example.ooad.domain.entity.InvoiceServiceDetail;
import com.example.ooad.repository.InvoiceMedicineDetailRepository;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.repository.InvoiceServiceDetailRepository;

@Component
public class InvoiceRefactor {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceServiceDetailRepository invoiceServiceDetailRepository;
    private final InvoiceMedicineDetailRepository invoiceMedicineDetailRepository;
    public InvoiceRefactor(InvoiceRepository invoiceRepository, InvoiceServiceDetailRepository invoiceServiceDetailRepository, InvoiceMedicineDetailRepository invoiceMedicineDetailRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceServiceDetailRepository= invoiceServiceDetailRepository;
        this.invoiceMedicineDetailRepository = invoiceMedicineDetailRepository;
        List<Invoice> listInvoices = invoiceRepository.findAll();
        for(Invoice invoice: listInvoices) {
            calculateTotal(invoice);
        }
        invoiceRepository.saveAll(listInvoices);
    }
    private void calculateTotal(Invoice invoice) {
        List<InvoiceServiceDetail> listServiceDetails = invoiceServiceDetailRepository.findByInvoice_InvoiceId(invoice.getInvoiceId());
        List<InvoiceMedicineDetail> listMedicineDetails = invoiceMedicineDetailRepository.findByInvoice_InvoiceId(invoice.getInvoiceId());
        BigDecimal servicePrice = calculateMoneyServiceFromList(listServiceDetails);
        BigDecimal medicinePrice = calculateMoneyMedicineFromList(listMedicineDetails);
        invoice.setMedicineFee(medicinePrice);
        invoice.setServiceFee(servicePrice);
        invoice.setTotalAmount(medicinePrice.add(servicePrice));

    }
    private BigDecimal calculateMoneyServiceFromList(List<InvoiceServiceDetail> listDetails) {
        BigDecimal res=new BigDecimal(0);
        for(InvoiceDetail invoiceDetail: listDetails) {
            res=res.add(invoiceDetail.getSalePrice());

        }
        return res;
    }
    private BigDecimal calculateMoneyMedicineFromList(List<InvoiceMedicineDetail> listDetails) {
        BigDecimal res=new BigDecimal(0);
        for(InvoiceDetail invoiceDetail: listDetails) {
            res=res.add(invoiceDetail.getSalePrice());

        }
        return res;
    }

}
