package com.example.ooad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDetailDto {
    private MedicineDto medicine;
    private Integer quantity;
    private String dosage;
    private Integer days;
    private String dispenseStatus;
    private boolean isMorning;
    private boolean isAfterNoon;
    private boolean isEvening;
    private boolean isNight;

    
   
}
