package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseTrackerRepo extends JpaRepository<ExpenseTracker,Long> {
    boolean existsByName(String name);

    @Query(value = "SELECT et FROM expense_tracker et WHERE et.id = :expenseTrackerId AND et.user.id = :userId" )
    Optional<ExpenseTracker> findByIdAndUserId(@Param("expenseTrackerId")Long expenseTrackerId, @Param("userId") Long userId);

    @Query(value = "SELECT et FROM expense_tracker et WHERE et.user.id = :userId")
    List<ExpenseTracker> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "DELETE FROM expense_tracker et WHERE et.id = :expenseTrackerId AND et.user.id = :userId")
    void deleteExpenseTrById(@Param("expenseTrackerId") Long expenseTrackerId, @Param("userId") Long userId);

}
