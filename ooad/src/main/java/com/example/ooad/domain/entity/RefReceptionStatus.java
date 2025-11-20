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

@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor

//@Entity
@Table(name="ref_reception_status")
public class RefReceptionStatus extends ReferenceStatus  {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int receptionStatusId;*/

    
}
