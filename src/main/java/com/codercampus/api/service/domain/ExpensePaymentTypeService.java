package com.codercampus.api.service.domain;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpensePaymentType;
import com.codercampus.api.repository.resource.ExpenseAddressRepo;
import com.codercampus.api.repository.resource.ExpensePaymentTypeRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpensePaymentTypeService {

    private final UserService userService;
    private final ExpenseTrackerService expenseTrackerService;
    private final ExpensePaymentTypeRepo expensePaymentTypeRepo;

    public ExpensePaymentTypeService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpensePaymentTypeRepo expensePaymentTypeRepo
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expensePaymentTypeRepo = expensePaymentTypeRepo;
    }

    /**
     *
     * @param expensePaymentType
     * @return
     */
    public ExpensePaymentType save(ExpensePaymentType expensePaymentType){
        return this.expensePaymentTypeRepo.save(expensePaymentType);
    }

    /**
     *
     * @param expensePaymentType
     * @return
     */
    public Optional<ExpensePaymentType> createIfNotExists(ExpensePaymentType expensePaymentType){

        if(this.expensePaymentTypeRepo.existsByName(expensePaymentType.getName())){
            return Optional.empty();
        }

        UserDetailsImpl userDetails = this.userService.getUserDetails();

        expensePaymentType.setCreatedBy(userDetails.getUsername());
        expensePaymentType.setUpdatedBy(userDetails.getUsername());
//        expensePaymentType.addExpense(expense);
        return Optional.of(this.save(expensePaymentType));
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.expensePaymentTypeRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpensePaymentType> findById(Long id){
        return this.expensePaymentTypeRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<ExpensePaymentType> findAll(){
        return this.expensePaymentTypeRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpensePaymentType> deleteById(Long id){
        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeRepo.findById(id);

        if(expensePaymentTypeOpt.isPresent()){
            ExpensePaymentType expensePaymentType = expensePaymentTypeOpt.get();

            for(Expense expense : expensePaymentType.getExpenses()){
                expensePaymentType.removeExpense(expense);
            }

            this.expensePaymentTypeRepo.deleteById(id);

            return expensePaymentTypeOpt;
        }
        return expensePaymentTypeOpt;
    }

    /**
     *
     * @param expensePaymentType
     * @return
     */
    public ExpensePaymentType update(ExpensePaymentType expensePaymentType ){


        return this.save(expensePaymentType);
    }
}
