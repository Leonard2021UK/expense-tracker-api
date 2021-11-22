package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseTypeRepo extends JpaRepository<ExpenseType,Long> {
    boolean existsByName(String name);
}
