package com.codercampus.api.service.domain;

import com.codercampus.api.model.*;
import com.codercampus.api.repository.resource.ExpenseItemRepo;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseItemService {

    private final UserService userService;

    private final ExpenseItemRepo expenseItemRepo;

    public ExpenseItemService(UserService userService, ExpenseItemRepo expenseItemRepo) {
        this.userService = userService;
        this.expenseItemRepo = expenseItemRepo;
    }


    public List<ExpenseItem> saveAll(List<ExpenseItem> expenseItemList){
        return this.expenseItemRepo.saveAll(expenseItemList);
    }

    public ExpenseItem save(ExpenseItem expenseItem){
        return this.expenseItemRepo.save(expenseItem);
    }

}
