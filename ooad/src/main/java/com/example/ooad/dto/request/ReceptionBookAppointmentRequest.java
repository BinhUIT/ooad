package com.example.ooad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceptionBookAppointmentRequest extends BookAppointmentRequest{
    private int patientId;
    public BookAppointmentRequest getRequest() {
        return new BookAppointmentRequest(this.scheduleId);
    }
}
