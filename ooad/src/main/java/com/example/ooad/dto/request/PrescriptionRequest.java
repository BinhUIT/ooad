package com.example.ooad.dto.request;

import java.util.List;

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
public class PrescriptionRequest {
    private int recordId;
    private String notes;
    private List<PrescriptionDetailRequest> prescriptionDetails;
}
