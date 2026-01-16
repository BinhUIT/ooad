package com.example.ooad.domain.entity;

import com.example.ooad.domain.compositekey.PrescriptionDetailKey;
import com.example.ooad.domain.enums.EDispenseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="prescription_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDetail {
    @EmbeddedId
    private PrescriptionDetailKey prescriptionDetailId;
    @ManyToOne
    @MapsId("prescriptionId")
    @JoinColumn(name="prescription_id")
    private Prescription prescription;
    @ManyToOne
    @MapsId("medicineId")
    @JoinColumn(name="medicine_id")
    private Medicine medicine;
    private int quantity;
    private boolean isMorning;
    private boolean isAfterNoon;
    private boolean isEvening;
    private boolean isNight;
    @Column(name="dosage", columnDefinition="VARCHAR(100)")
    private String dosage;
    private int days;
    @Enumerated(EnumType.STRING)
    private EDispenseStatus dispenseStatus=EDispenseStatus.PENDING;
    
}
