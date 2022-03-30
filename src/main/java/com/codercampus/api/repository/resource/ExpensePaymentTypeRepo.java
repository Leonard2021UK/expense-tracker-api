package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpensePaymentType;
import com.codercampus.api.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpensePaymentTypeRepo extends JpaRepository<ExpensePaymentType,Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT ept FROM expense_payment_type ept where ept.user.id = :userId")
    List<ExpensePaymentType> findAllByUserId(Long userId);

    Optional<ExpensePaymentType> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query(value = "DELETE FROM expense_payment_type ea WHERE ea.id = :expensePaymentTypeId AND ea.user.id = :userId")
    void deleteById(@Param("expensePaymentTypeId") Long expensePaymentTypeId, @Param("userId") Long userId);

}
