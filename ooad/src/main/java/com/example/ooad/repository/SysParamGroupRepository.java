package com.example.ooad.repository;

import com.example.ooad.domain.entity.SysParamGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysParamGroupRepository extends JpaRepository<SysParamGroup, Integer> {

   Optional<SysParamGroup> findByGroupCode(String groupCode);

   boolean existsByGroupCode(String groupCode);

   @Query("SELECT spg FROM SysParamGroup spg WHERE spg.isActive = true")
   List<SysParamGroup> findAllActive();

   @Query("SELECT spg FROM SysParamGroup spg WHERE spg.groupCode LIKE %:keyword% OR spg.groupName LIKE %:keyword%")
   org.springframework.data.domain.Page<SysParamGroup> findByGroupCodeContainingOrGroupNameContaining(String keyword,
         String keyword2, org.springframework.data.domain.Pageable pageable);

   @Query("SELECT spg FROM SysParamGroup spg ORDER BY spg.sortOrder ASC")
   List<SysParamGroup> findAllOrderBySortOrder();
}