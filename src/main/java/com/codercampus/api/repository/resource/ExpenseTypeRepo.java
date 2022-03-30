package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseTypeRepo extends JpaRepository<ExpenseType,Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT et FROM expense_type et where et.user.id = :userId")
    List<ExpenseType> findAllByUserId(Long userId);

    Optional<ExpenseType> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query(value = "DELETE FROM expense_type ea WHERE ea.id = :expenseTypeId AND ea.user.id = :userId")
    void deleteById(@Param("expenseTypeId") Long expenseTypeId, @Param("userId") Long userId);

}
