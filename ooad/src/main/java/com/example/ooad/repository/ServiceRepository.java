package com.example.ooad.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ooad.domain.entity.Service;
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    public Page<Service> findAll(Pageable pageable);
    public Service findByServiceName(String serviceName);
    @Query("SELECT s from Service s WHERE "+ "(:keyWord is null or UPPER(s.serviceName) LIKE UPPER(CONCAT('%',:keyWord,'%')))")
    public Page<Service> searchService(Pageable pageable, @Param("keyWord") String keyWord);
}
