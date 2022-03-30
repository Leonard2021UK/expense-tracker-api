package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UnitTypeRepo extends JpaRepository<UnitType,Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT Ut FROM unit_type Ut WHERE Ut.isArchived = false AND Ut.user.id = :userId")
    List<UnitType> findAllNoneArchived(Long userId);

    @Query(value = "SELECT Ut FROM unit_type Ut WHERE Ut.isArchived = false AND Ut.id = :unitTypeId AND Ut.user.id = :userId")
    Optional<UnitType> findById(Long unitTypeId, Long userId);

    @Modifying
    @Query(value = "DELETE FROM unit_type Ut WHERE Ut.id = :unitTypeId AND Ut.user.id = :userId")
    void deleteById(@Param("unitTypeId") Long unitTypeId, @Param("userId") Long userId);



}
