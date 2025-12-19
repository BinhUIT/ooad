package com.example.ooad.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.compositekey.PrescriptionDetailKey;
import com.example.ooad.domain.entity.PrescriptionDetail;
@Repository
public interface PrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, PrescriptionDetailKey> {
    public Page<PrescriptionDetail> findByPrescription_PrescriptionId(Pageable pageable, int prescriptionId);
    public List<PrescriptionDetail> findByPrescription_PrescriptionId(int prescriptionId);
}