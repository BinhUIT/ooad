package com.example.ooad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentChartData {
    private String month;
    private int completed;
    private int notshown;
    private int cancelled;
    private int waiting;
    private int confirmed;
}
