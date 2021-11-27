package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitTypeRepo extends JpaRepository<UnitType,Long> {
    boolean existsByName(String name);
}
