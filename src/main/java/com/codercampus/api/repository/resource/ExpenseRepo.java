package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpensePaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {
    boolean existsByExpenseName(String name);

//    Expense getExpenseById(Long id);

    Optional<Expense> findById(Long id);
}
