package com.example.ooad.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrescriptionDetailRequest {
    private int prescriptionId;
    private int medicineId;
    private int quantity;
    private String dosage;
    private int days;

}
