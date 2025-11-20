package com.example.ooad.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Table(name="ref_appointment_status")
public class RefAppointmentStatus extends ReferenceStatus {
    /*@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="appointment_status_id")
    private int appointmentStatusId;*/
   
   
    
    
}
