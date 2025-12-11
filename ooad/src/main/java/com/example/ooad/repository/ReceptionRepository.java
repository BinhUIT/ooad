package com.example.ooad.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.ooad.domain.entity.Reception;

public interface ReceptionRepository extends JpaRepository<Reception, Integer> {
    public Page<Reception> findAllByOrderByReceptionDateDesc(Pageable pageable);
    public List<Reception> findByPatient_PatientId(int patientId);
}
