package com.codercampus.api.service.domain;

import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
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
        expensePaymentType.setUser(userDetails.getUser());
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

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.expensePaymentTypeRepo.findByIdAndUserId(id,userDetails.getUser().getId());
    }

    /**
     *
     * @return
     */
    public List<ExpensePaymentType> findAll(){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.expensePaymentTypeRepo.findAllByUserId(userDetails.getUser().getId());
    }

    /**
     *
     * @param id
     * @return
     */
    public ExpensePaymentType deleteById(Long id) throws ResourceHasReferenceException, ResourceNotFoundException {
        Long currentUserId = this.userService.getUserDetails().getUser().getId();

        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeRepo.findByIdAndUserId(id,currentUserId);

        if(expensePaymentTypeOpt.isPresent()){
            ExpensePaymentType expensePaymentType = expensePaymentTypeOpt.get();

            if (expensePaymentType.getExpenses().isEmpty()) {
                this.expensePaymentTypeRepo.deleteById(id,currentUserId);
                return expensePaymentType;
            }else{
                throw ResourceHasReferenceException.createWith("Expense payment type");

            }
        }
        throw ResourceNotFoundException.createWith("Expense type could not be found!");
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
