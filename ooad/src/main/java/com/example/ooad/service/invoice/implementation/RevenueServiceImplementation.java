package com.example.ooad.service.invoice.implementation;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ooad.domain.entity.Invoice;
import com.example.ooad.domain.entity.Staff;
import com.example.ooad.domain.enums.EPaymentStatus;
import com.example.ooad.dto.response.DoctorRevenueResponse;
import com.example.ooad.dto.response.DoctorRevenueResponse.MonthlyDoctorRevenue;
import com.example.ooad.dto.response.RevenueStatisticResponse;
import com.example.ooad.dto.response.RevenueStatisticResponse.DailyRevenueData;
import com.example.ooad.dto.response.RevenueStatisticResponse.MonthlyRevenueData;
import com.example.ooad.repository.InvoiceRepository;
import com.example.ooad.service.invoice.interfaces.RevenueService;

@Service
public class RevenueServiceImplementation implements RevenueService {
    
    private final InvoiceRepository invoiceRepository;
    private final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    
    public RevenueServiceImplementation(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }
    
    @Override
    public DailyRevenueData getDailyRevenue(Date date) {
        List<Invoice> invoices = invoiceRepository.findByInvoiceDateAndStatus(date, EPaymentStatus.PAID);
        return calculateDailyRevenue(date.toString(), invoices);
    }
    
    @Override
    public DailyRevenueData getDailyRevenueByDoctor(Date date, Staff doctor) {
        List<Invoice> invoices = invoiceRepository.findByInvoiceDateAndStatusAndDoctor(date, EPaymentStatus.PAID, doctor);
        return calculateDailyRevenue(date.toString(), invoices);
    }
    
    @Override
    public RevenueStatisticResponse getMonthlyRevenueByYear(int year) {
        List<Invoice> yearInvoices = invoiceRepository.findByYearAndStatus(year, EPaymentStatus.PAID);
        
        RevenueStatisticResponse response = new RevenueStatisticResponse();
        response.setTotalRevenue(BigDecimal.ZERO);
        response.setExaminationRevenue(BigDecimal.ZERO);
        response.setMedicineRevenue(BigDecimal.ZERO);
        response.setServiceRevenue(BigDecimal.ZERO);
        response.setTotalInvoices(yearInvoices.size());
        
        List<MonthlyRevenueData> monthlyData = new ArrayList<>();
        
        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            List<Invoice> monthInvoices = yearInvoices.stream()
                .filter(i -> {
                    LocalDate invoiceDate = i.getInvoiceDate().toLocalDate();
                    return invoiceDate.getMonthValue() == currentMonth;
                })
                .toList();
            
            MonthlyRevenueData monthData = calculateMonthlyRevenue(monthNames[month - 1], month, monthInvoices);
            monthlyData.add(monthData);
            
            response.setTotalRevenue(response.getTotalRevenue().add(monthData.getRevenue()));
            response.setExaminationRevenue(response.getExaminationRevenue().add(monthData.getExaminationFee()));
            response.setMedicineRevenue(response.getMedicineRevenue().add(monthData.getMedicineFee()));
            response.setServiceRevenue(response.getServiceRevenue().add(monthData.getServiceFee()));
        }
        
        response.setMonthlyRevenue(monthlyData);
        return response;
    }
    
    @Override
    public DoctorRevenueResponse getMonthlyRevenueByYearAndDoctor(int year, Staff doctor) {
        return getDoctorRevenueStatistics(doctor, year);
    }
    
    @Override
    public DoctorRevenueResponse getDoctorRevenueStatistics(Staff doctor, int year) {
        List<Invoice> yearInvoices = invoiceRepository.findByYearAndStatusAndDoctor(year, EPaymentStatus.PAID, doctor);
        
        DoctorRevenueResponse response = new DoctorRevenueResponse();
        response.setDoctorId(doctor.getStaffId());
        response.setDoctorName(doctor.getFullName());
        response.setTotalRevenue(BigDecimal.ZERO);
        response.setExaminationRevenue(BigDecimal.ZERO);
        response.setMedicineRevenue(BigDecimal.ZERO);
        response.setServiceRevenue(BigDecimal.ZERO);
        response.setTotalInvoices(yearInvoices.size());
        
        List<MonthlyDoctorRevenue> monthlyData = new ArrayList<>();
        
        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            List<Invoice> monthInvoices = yearInvoices.stream()
                .filter(i -> {
                    LocalDate invoiceDate = i.getInvoiceDate().toLocalDate();
                    return invoiceDate.getMonthValue() == currentMonth;
                })
                .toList();
            
            MonthlyDoctorRevenue monthData = calculateMonthlyDoctorRevenue(monthNames[month - 1], month, year, monthInvoices);
            monthlyData.add(monthData);
            
            response.setTotalRevenue(response.getTotalRevenue().add(monthData.getRevenue()));
            response.setExaminationRevenue(response.getExaminationRevenue().add(monthData.getExaminationFee()));
            response.setMedicineRevenue(response.getMedicineRevenue().add(monthData.getMedicineFee()));
            response.setServiceRevenue(response.getServiceRevenue().add(monthData.getServiceFee()));
        }
        
        response.setMonthlyRevenue(monthlyData);
        return response;
    }
    
    private DailyRevenueData calculateDailyRevenue(String date, List<Invoice> invoices) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal examinationFee = BigDecimal.ZERO;
        BigDecimal medicineFee = BigDecimal.ZERO;
        BigDecimal serviceFee = BigDecimal.ZERO;
        
        for (Invoice invoice : invoices) {
            totalRevenue = totalRevenue.add(invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO);
            examinationFee = examinationFee.add(invoice.getExaminationFee() != null ? invoice.getExaminationFee() : BigDecimal.ZERO);
            medicineFee = medicineFee.add(invoice.getMedicineFee() != null ? invoice.getMedicineFee() : BigDecimal.ZERO);
            serviceFee = serviceFee.add(invoice.getServiceFee() != null ? invoice.getServiceFee() : BigDecimal.ZERO);
        }
        
        return new DailyRevenueData(date, totalRevenue, examinationFee, medicineFee, serviceFee, invoices.size());
    }
    
    private MonthlyRevenueData calculateMonthlyRevenue(String monthName, int monthNumber, List<Invoice> invoices) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal examinationFee = BigDecimal.ZERO;
        BigDecimal medicineFee = BigDecimal.ZERO;
        BigDecimal serviceFee = BigDecimal.ZERO;
        
        for (Invoice invoice : invoices) {
            totalRevenue = totalRevenue.add(invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO);
            examinationFee = examinationFee.add(invoice.getExaminationFee() != null ? invoice.getExaminationFee() : BigDecimal.ZERO);
            medicineFee = medicineFee.add(invoice.getMedicineFee() != null ? invoice.getMedicineFee() : BigDecimal.ZERO);
            serviceFee = serviceFee.add(invoice.getServiceFee() != null ? invoice.getServiceFee() : BigDecimal.ZERO);
        }
        
        return new MonthlyRevenueData(monthName, monthNumber, totalRevenue, examinationFee, medicineFee, serviceFee, invoices.size());
    }
    
    private MonthlyDoctorRevenue calculateMonthlyDoctorRevenue(String monthName, int monthNumber, int year, List<Invoice> invoices) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal examinationFee = BigDecimal.ZERO;
        BigDecimal medicineFee = BigDecimal.ZERO;
        BigDecimal serviceFee = BigDecimal.ZERO;
        
        for (Invoice invoice : invoices) {
            totalRevenue = totalRevenue.add(invoice.getTotalAmount() != null ? invoice.getTotalAmount() : BigDecimal.ZERO);
            examinationFee = examinationFee.add(invoice.getExaminationFee() != null ? invoice.getExaminationFee() : BigDecimal.ZERO);
            medicineFee = medicineFee.add(invoice.getMedicineFee() != null ? invoice.getMedicineFee() : BigDecimal.ZERO);
            serviceFee = serviceFee.add(invoice.getServiceFee() != null ? invoice.getServiceFee() : BigDecimal.ZERO);
        }
        
        return new MonthlyDoctorRevenue(monthName, monthNumber, year, totalRevenue, examinationFee, medicineFee, serviceFee, invoices.size());
    }
}
