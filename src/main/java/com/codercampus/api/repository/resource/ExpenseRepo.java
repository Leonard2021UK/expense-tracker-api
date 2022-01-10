package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepo extends JpaRepository<Expense,Long> {
    boolean existsByExpenseName(String name);
}
