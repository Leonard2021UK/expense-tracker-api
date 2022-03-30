package com.codercampus.api.service.view;

import com.codercampus.api.model.views.ExpenseTrackerView;
import com.codercampus.api.model.views.ItemCategoryView;
import com.codercampus.api.repository.views.ExpenseTrackerSumViewRepo;
import com.codercampus.api.repository.views.ItemCategorySumViewRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    ExpenseTrackerSumViewRepo expenseTrackerSumViewRepo;
    ItemCategorySumViewRepo itemCategorySumViewRepo;
    UserService userService;

    public DashboardService(
            ItemCategorySumViewRepo itemCategorySumViewRepo,
            ExpenseTrackerSumViewRepo expenseTrackerSumViewRepo,
            UserService userService){
        this.expenseTrackerSumViewRepo = expenseTrackerSumViewRepo;
        this.userService = userService;
        this.itemCategorySumViewRepo = itemCategorySumViewRepo;
    }

    public List<ExpenseTrackerView> findAll(){
        UserDetailsImpl userDetails = this.userService.getUserDetails();

        return this.expenseTrackerSumViewRepo.getExpenseTrackerSumByUserId(userDetails.getUser().getId());
    }

    public List<ItemCategoryView> findAllItemCategoryByPeriod(){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.itemCategorySumViewRepo.findAllItemCategoryByPeriod(userDetails.getUser().getId());
    }
}
