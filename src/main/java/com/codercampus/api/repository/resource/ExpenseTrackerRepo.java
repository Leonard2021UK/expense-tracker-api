package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseTrackerRepo extends JpaRepository<ExpenseTracker,Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT Et FROM ExpenseTracker Et WHERE Et.id = :expenseTrackerId AND Et.user.id = :userId")
    Optional<ExpenseTracker> findById(Long expenseTrackerId, Long userId);

    @Query(value = "SELECT Et FROM ExpenseTracker Et WHERE Et.user.id = :userId")
    List<ExpenseTracker> findAll(Long userId);
}
