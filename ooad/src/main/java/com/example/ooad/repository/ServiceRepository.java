package com.example.ooad.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Service;
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    public Page<Service> findAll(Pageable pageable);
    public Service findByServiceName(String serviceName);
}
