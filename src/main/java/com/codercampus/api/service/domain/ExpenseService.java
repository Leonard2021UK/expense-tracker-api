package com.codercampus.api.service.domain;

import com.codercampus.api.model.*;
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
    private final ExpensePaymentTypeService expensePaymentTypeService;
    private final ExpenseTypeService expenseTypeService;
    public ExpenseService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseRepo expenseRepo,
            ExpenseAddressService expenseAddressService,
            ExpenseTypeService expenseTypeService,
            ExpensePaymentTypeService expensePaymentTypeService
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseRepo = expenseRepo;
        this.expenseAddressService = expenseAddressService;
        this.expensePaymentTypeService = expensePaymentTypeService;
        this.expenseTypeService = expenseTypeService;
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
    public Optional<Expense> createIfNotExists(
            Expense expense,
            Long expenseTrackerId,
            Long expenseAddressId,
            Long expenseTypeId,
            Long expensePaymentTypeId
    ){

        if(this.expenseRepo.existsByName(expense.getName())){
            return Optional.empty();
        }

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(expenseTrackerId);
        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.findById(expenseAddressId);
        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.findById(expenseTypeId);
        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeService.findById(expensePaymentTypeId);

        if(expenseTrackerOpt.isPresent() && expenseAddressOpt.isPresent() && expenseTypeOpt.isPresent() && expensePaymentTypeOpt.isPresent()) {

            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
            ExpenseAddress expenseAddress = expenseAddressOpt.get();
            ExpenseType expenseType = expenseTypeOpt.get();
            ExpensePaymentType expensePaymentType = expensePaymentTypeOpt.get();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            expenseTracker.addExpense(expense);
            expense.setExpenseTracker(expenseTracker);

            expenseAddress.addExpense(expense);
            expense.setExpenseAddress(expenseAddress);

            expenseType.addExpense(expense);
            expense.setExpenseType(expenseType);

            expensePaymentType.addExpense(expense);
            expense.setExpensePaymentType(expensePaymentType);

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

//    /**
//     *
//     * @param id
//     * @return
//     */
//    public Optional<Expense> deleteById(Long id){
//
//        Optional<Expense> expenseOpt = this.expenseRepo.findById(id);
//
//        if(expenseOpt.isPresent()){
//            Expense expense = expenseOpt.get();
//            expense.getExpenseTracker().removeExpense(expense);
//            for(Item item : expense.getItems()){
//                item.getExpenses().remove(expense);
//            }
//            return Optional.of(this.expenseRepo.save(expense));
//        }
//        return Optional.empty();
//    }

    /**
     *
     * @param expense
     * @param expenseTracker
     * @param expenseAddress
     * @param expenseType
     * @param expensePaymentType
     * @return
     */
    public Expense update(Expense expense, ExpenseTracker expenseTracker, ExpenseAddress expenseAddress,ExpenseType expenseType, ExpensePaymentType expensePaymentType ){

        expense.setExpenseTracker(expenseTracker);
        expense.setExpenseAddress(expenseAddress);
        expense.setExpenseType(expenseType);
        expense.setExpensePaymentType(expensePaymentType);

        return this.save(expense);
    }
}
