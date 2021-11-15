package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseTrackerRepo extends JpaRepository<ExpenseTracker,Long> {

}
