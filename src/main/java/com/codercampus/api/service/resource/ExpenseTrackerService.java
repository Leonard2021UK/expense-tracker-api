package com.codercampus.api.service.resource;

import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.repository.resource.ExpenseTrackerRepo;
import com.codercampus.api.repository.resource.MainCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseTrackerService {

    private final ExpenseTrackerRepo expenseTrackerRepo;

    public ExpenseTrackerService(ExpenseTrackerRepo expenseTrackerRepo) {
        this.expenseTrackerRepo = expenseTrackerRepo;
    }

    public ExpenseTracker save(ExpenseTracker expenseTracker){
        return this.expenseTrackerRepo.save(expenseTracker);
    }

    public Optional<ExpenseTracker> findById(Long id){
        return this.expenseTrackerRepo.findById(id);
    }

    public List<ExpenseTracker> findAll(){
        return this.expenseTrackerRepo.findAll();
    }

    public void deleteById(Long id){
        this.expenseTrackerRepo.deleteById(id);
    }

    public ExpenseTracker updateMainCategory(ExpenseTracker expenseTracker){
        return this.save(expenseTracker);
    }
}
