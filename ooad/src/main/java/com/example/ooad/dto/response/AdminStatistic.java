package com.example.ooad.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatistic {
    private int todayAppointment;
    private int staffToday;
    private BigDecimal thisMothIncome;
}
