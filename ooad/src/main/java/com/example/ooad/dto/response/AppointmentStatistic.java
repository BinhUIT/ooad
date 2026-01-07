package com.example.ooad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AppointmentStatistic {
    private int completed;
    private int noshow;
    private int scheduled;
    private int cancelled;
    private int total;
}
