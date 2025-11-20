package com.example.ooad.domain.entity;

import java.sql.Date;
import java.time.LocalTime;

import com.example.ooad.domain.enums.EAppointmentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private int appointmentId;
    @ManyToOne
    @JoinColumn(name="patient_id")
    private Patient patient;
    @ManyToOne
    @JoinColumn(name="doctor_id")
    private Staff staff;
    private Date appointmentDate;
    private LocalTime appointmentTime;
    @Enumerated(EnumType.STRING)
    private EAppointmentStatus status=EAppointmentStatus.SCHEDULED;
    private Date createDate;

    public Appointment() {
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public EAppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(EAppointmentStatus status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Appointment(int appointmentId, Patient patient, Staff staff, Date appointmentDate, LocalTime appointmentTime,
            EAppointmentStatus status, Date createDate) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.staff = staff;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.createDate = createDate;
    }

}
