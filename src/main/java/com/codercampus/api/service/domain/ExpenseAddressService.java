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
    private final ExpenseService expenseService;

    public ExpenseAddressService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseAddressRepo expenseAddressRepo,
            ExpenseService expenseService
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseAddressRepo = expenseAddressRepo;
        this.expenseService = expenseService;
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
    public Optional<ExpenseAddress> createIfNotExists(ExpenseAddress expenseAddress, Long expenseId){

        if(this.expenseAddressRepo.existsByAddressLine1AndPostCode(expenseAddress.getAddressLine1(),expenseAddress.getPostCode())){
            return Optional.empty();
        }

        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

        if(expenseOpt.isPresent()){
            Expense expense = expenseOpt.get();
            // read currently logged-in user into UserService
            this.userService.setSecurityContext();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            expenseAddress.setCreatedBy(userDetails.getUsername());
            expenseAddress.setUpdatedBy(userDetails.getUsername());
            expenseAddress.addExpense(expense);
            return Optional.of(this.save(expenseAddress));
        }
        return Optional.empty();

    }

    /**
     *
     * @param expenseAddress
     * @return
     */
    public boolean isExists(ExpenseAddress expenseAddress){
        return this.expenseAddressRepo.existsByAddressLine1AndPostCode(expenseAddress.getAddressLine1(),expenseAddress.getPostCode());
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
