package com.example.ooad.domain.entity;

import java.sql.Date;

import com.example.ooad.domain.enums.EReceptionStatus;

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
@Table(name="reception")
public class Reception {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int receptionId;
    @ManyToOne
    @JoinColumn(name="patient_id")
    private Patient patient;
    private Date receptionDate;
    @Enumerated(EnumType.STRING)
    private EReceptionStatus status=EReceptionStatus.WAITING;
    @ManyToOne
    @JoinColumn(name="receptionist_id")
    private Staff receptionist;
    public Reception() {
    }
    public Reception(int receptionId, Patient patient, Date receptionDate, EReceptionStatus status,
            Staff receptionist) {
        this.receptionId = receptionId;
        this.patient = patient;
        this.receptionDate = receptionDate;
        this.status = status;
        this.receptionist = receptionist;
    }

    public int getReceptionId() {
        return receptionId;
    }

    public void setReceptionId(int receptionId) {
        this.receptionId = receptionId;
    }

    public Patient getPatient() {
        if(patient==null) {
            return new Patient(0, "", null, null, "", "", "", "", null, null, null);
        }
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(Date receptionDate) {
        this.receptionDate = receptionDate;
    }

    public EReceptionStatus getStatus() {
        return status;
    }

    public void setStatus(EReceptionStatus status) {
        this.status = status;
    }

    public Staff getReceptionist() {
        if(receptionist==null) {
            return new Staff("", 0, "", null, null, "", "", "", null);
        }
        return receptionist;
    }

    public void setReceptionist(Staff receptionist) {
        this.receptionist = receptionist;
    }
    
}
