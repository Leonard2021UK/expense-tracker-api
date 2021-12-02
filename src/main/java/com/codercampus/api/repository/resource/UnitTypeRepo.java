package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UnitTypeRepo extends JpaRepository<UnitType,Long> {
    boolean existsByName(String name);
    Set<UnitType> findAllByArchivedFalseAndUser(Long userId);
}
