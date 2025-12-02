package com.example.ooad.repository;

import com.example.ooad.domain.entity.SysParamGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysParamGroupRepository extends JpaRepository<SysParamGroup, Integer> {
    Optional<SysParamGroup> findByGroupCode(String groupCode);

    boolean existsByGroupCode(String groupCode);

    @Query("SELECT spg FROM SysParamGroup spg WHERE spg.isActive = true")
    List<SysParamGroup> findAllActive();

    @Query("SELECT spg FROM SysParamGroup spg WHERE spg.groupCode LIKE %:keyword% OR spg.groupName LIKE %:keyword%")
    Page<SysParamGroup> findByGroupCodeContainingOrGroupNameContaining(String keyword,
            String keyword2,
            Pageable pageable);
}
