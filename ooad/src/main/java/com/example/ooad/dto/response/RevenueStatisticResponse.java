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
public class RevenueStatisticResponse {
    private BigDecimal totalRevenue;
    private BigDecimal examinationRevenue;
    private BigDecimal medicineRevenue;
    private BigDecimal serviceRevenue;
    private int totalInvoices;
    private List<DailyRevenueData> dailyRevenue;
    private List<MonthlyRevenueData> monthlyRevenue;
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyRevenueData {
        private String date;
        private BigDecimal revenue;
        private BigDecimal examinationFee;
        private BigDecimal medicineFee;
        private BigDecimal serviceFee;
        private int invoiceCount;
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyRevenueData {
        private String month;
        private int monthNumber;
        private BigDecimal revenue;
        private BigDecimal examinationFee;
        private BigDecimal medicineFee;
        private BigDecimal serviceFee;
        private int invoiceCount;
    }
}
