package com.codercampus.api.service.domain;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.repository.resource.ExpenseAddressRepo;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseAddressService {

    private final UserService userService;
    private final ExpenseTrackerService expenseTrackerService;
    private final ExpenseAddressRepo expenseAddressRepo;

    public ExpenseAddressService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseAddressRepo expenseAddressRepo
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseAddressRepo = expenseAddressRepo;
    }

    /**
     *
     * @param expenseAddress
     * @return
     */
    public ExpenseAddress save(ExpenseAddress expenseAddress){
        return this.expenseAddressRepo.save(expenseAddress);
    }

    /**
     *
     * @param expenseAddress
     * @return
     */
    public Optional<ExpenseAddress> createIfNotExists(ExpenseAddress expenseAddress){

        if(this.expenseAddressRepo.existsByName(expenseAddress.getName())){
            return Optional.empty();
        }

        UserDetailsImpl userDetails = this.userService.getUserDetails();

        expenseAddress.setCreatedBy(userDetails.getUsername());
        expenseAddress.setUpdatedBy(userDetails.getUsername());
//        expenseAddress.addExpense(expense);
        return Optional.of(this.save(expenseAddress));
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.expenseAddressRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpenseAddress> findById(Long id){
        return this.expenseAddressRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<ExpenseAddress> findAll(){
        return this.expenseAddressRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpenseAddress> deleteById(Long id){
        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressRepo.findById(id);

        if(expenseAddressOpt.isPresent()){
            ExpenseAddress expenseAddress = expenseAddressOpt.get();

            for(Expense expense : expenseAddress.getExpenses()){
                expenseAddress.removeExpense(expense);
            }

            this.expenseAddressRepo.deleteById(id);

            return expenseAddressOpt;
        }
        return expenseAddressOpt;
    }

    /**
     *
     * @param expenseAddress
     * @return
     */
    public ExpenseAddress update(ExpenseAddress expenseAddress ){


        return this.save(expenseAddress);
    }
}
