package com.codercampus.api.service.domain;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final UserService userService;
    private final ExpenseTrackerService expenseTrackerService;
    private final ExpenseRepo expenseRepo;
    private final ExpenseAddressService expenseAddressService;

    public ExpenseService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseRepo expenseRepo,
            ExpenseAddressService expenseAddressService
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseRepo = expenseRepo;
        this.expenseAddressService = expenseAddressService;
    }

    /**
     *
     * @param expense
     * @return
     */
    public Expense save(Expense expense){
        return this.expenseRepo.save(expense);
    }

    /**
     *
     * @param expense
     * @param expenseTrackerId
     * @return
     */
    public Optional<Expense> createIfNotExists(Expense expense,Long expenseTrackerId,Long expenseAddressId){

        if(this.expenseRepo.existsByName(expense.getName())){
            return Optional.empty();
        }

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(expenseTrackerId);
        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.findById(expenseAddressId);

        if(expenseTrackerOpt.isPresent() && expenseAddressOpt.isPresent()) {

            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
            ExpenseAddress expenseAddress = expenseAddressOpt.get();
            UserDetailsImpl userDetails = this.userService.getUserDetails();

            expenseTracker.addExpense(expense);
            expense.setExpenseTracker(expenseTracker);

            expenseAddress.addExpense(expense);
            expense.setExpenseAddress(expenseAddress);

            expense.setCreatedBy(userDetails.getUsername());
            expense.setUpdatedBy(userDetails.getUsername());

            return Optional.of(this.save(expense));

        }
        return Optional.empty();

    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.expenseRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Expense> findById(Long id){
        return this.expenseRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<Expense> findAll(){
        return this.expenseRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Expense> deleteById(Long id){

        Optional<Expense> expenseOpt = this.expenseRepo.findById(id);

        if(expenseOpt.isPresent()){
            Expense expense = expenseOpt.get();
            expense.getExpenseTracker().removeExpense(expense);
            return Optional.of(this.expenseRepo.save(expense));
        }
        return Optional.empty();
    }

    /**
     *
     * @param expense
     * @param expenseTracker
     * @param expenseAddress
     * @return
     */
    public Expense update(Expense expense, ExpenseTracker expenseTracker, ExpenseAddress expenseAddress ){
        expense.setExpenseTracker(expenseTracker);
        expense.setExpenseAddress(expenseAddress);

        return this.save(expense);
    }
}
