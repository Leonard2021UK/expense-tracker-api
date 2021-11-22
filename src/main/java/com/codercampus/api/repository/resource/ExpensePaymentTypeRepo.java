package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpensePaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensePaymentTypeRepo extends JpaRepository<ExpensePaymentType,Long> {
    boolean existsByName(String name);
}
