package com.codercampus.api.service.domain;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseType;
import com.codercampus.api.repository.resource.ExpenseTypeRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseTypeService {

    private final UserService userService;
    private final ExpenseTrackerService expenseTrackerService;
    private final ExpenseTypeRepo expenseTypeRepo;

    public ExpenseTypeService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseTypeRepo expenseTypeRepo
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTypeRepo = expenseTypeRepo;
    }

    /**
     *
     * @param expenseType
     * @return
     */
    public ExpenseType save(ExpenseType expenseType){
        return this.expenseTypeRepo.save(expenseType);
    }

    /**
     *
     * @param expenseType
     * @return
     */
    public Optional<ExpenseType> createIfNotExists(ExpenseType expenseType){

        if(this.expenseTypeRepo.existsByName(expenseType.getName())){
            return Optional.empty();
        }

        UserDetailsImpl userDetails = this.userService.getUserDetails();

        expenseType.setCreatedBy(userDetails.getUsername());
        expenseType.setUpdatedBy(userDetails.getUsername());
//        expenseType.addExpense(expense);
        return Optional.of(this.save(expenseType));
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.expenseTypeRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpenseType> findById(Long id){
        return this.expenseTypeRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<ExpenseType> findAll(){
        return this.expenseTypeRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpenseType> deleteById(Long id){

        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeRepo.findById(id);

        if(expenseTypeOpt.isPresent()){

            ExpenseType expenseType = expenseTypeOpt.get();

            for (Expense expense : expenseType.getExpenses()) {
                expenseType.removeExpense(expense);
            }

            this.expenseTypeRepo.deleteById(id);

        }
        return expenseTypeOpt;
    }

    /**
     *
     * @param expenseType
     * @return
     */
    public ExpenseType update(ExpenseType expenseType ){


        return this.save(expenseType);
    }
}
