package com.example.ooad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.BenhNhan;
@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan, Integer> {

}
