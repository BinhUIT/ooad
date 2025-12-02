package com.example.ooad.repository;

import com.example.ooad.domain.entity.SysParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysParamRepository extends JpaRepository<SysParam, Integer> {

    Optional<SysParam> findByParamCode(String paramCode);

    boolean existsByParamCode(String paramCode);

    @Query("SELECT sp FROM SysParam sp WHERE sp.group.id = :groupId")
    List<SysParam> findByGroupId(@Param("groupId") Integer groupId);

    @Query("SELECT sp FROM SysParam sp WHERE sp.group.groupCode = :groupCode")
    List<SysParam> findByGroupCode(@Param("groupCode") String groupCode);

    @Query("SELECT sp FROM SysParam sp WHERE sp.isActive = true")
    List<SysParam> findAllActive();

    @Query("SELECT sp FROM SysParam sp WHERE sp.group.isActive = true AND sp.isActive = true")
    List<SysParam> findAllActiveWithActiveGroup();

    @Query("SELECT sp FROM SysParam sp WHERE sp.paramCode LIKE %:keyword% OR sp.paramName LIKE %:keyword%")
    Page<SysParam> findByParamCodeContainingOrParamNameContaining(
            @Param("keyword") String keyword1, @Param("keyword") String keyword2,
            Pageable pageable);

    @Query("SELECT sp FROM SysParam sp WHERE (sp.paramCode LIKE %:keyword% OR sp.paramName LIKE %:keyword%) AND sp.group.id = :groupId")
    Page<SysParam> findByParamCodeContainingOrParamNameContainingAndGroupId(
            @Param("keyword") String keyword1, @Param("keyword") String keyword2,
            @Param("groupId") Integer groupId,
            Pageable pageable);

    @Query("SELECT sp FROM SysParam sp WHERE sp.group.id = :groupId")
    Page<SysParam> findByGroupId(@Param("groupId") Integer groupId,
            Pageable pageable);
}
