package com.example.ooad.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRevenueResponse {
    private int doctorId;
    private String doctorName;
    private BigDecimal totalRevenue;
    private BigDecimal examinationRevenue;
    private BigDecimal medicineRevenue;
    private BigDecimal serviceRevenue;
    private int totalInvoices;
    private List<MonthlyDoctorRevenue> monthlyRevenue;
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyDoctorRevenue {
        private String month;
        private int monthNumber;
        private int year;
        private BigDecimal revenue;
        private BigDecimal examinationFee;
        private BigDecimal medicineFee;
        private BigDecimal serviceFee;
        private int invoiceCount;
    }
}
