package com.example.ooad.dto.request;

import com.example.ooad.domain.enums.EReceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReceptionRequest {
    private int receptionId;
    private EReceptionStatus newStatus;
}
