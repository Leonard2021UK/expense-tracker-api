package com.codercampus.api.service.domain;

import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseAddress;
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
        expenseType.setUser(userDetails.getUser());
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
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.expenseTypeRepo.findByIdAndUserId(id,userDetails.getUser().getId());
    }
    /**
     *
     * @return
     */
    public List<ExpenseType> findAll(){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.expenseTypeRepo.findAllByUserId(userDetails.getUser().getId());
    }

    /**
     *
     * @param id
     * @return
     */
    public ExpenseType deleteById(Long id) throws ResourceHasReferenceException, ResourceNotFoundException {

        Long currentUserId = this.userService.getUserDetails().getUser().getId();
        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeRepo.findByIdAndUserId(id,currentUserId);

        if(expenseTypeOpt.isPresent()){

            ExpenseType expenseType = expenseTypeOpt.get();

            if (expenseType.getExpenses().isEmpty()) {
                this.expenseTypeRepo.deleteById(id,currentUserId);
                return expenseType;
            }else{
                throw ResourceHasReferenceException.createWith("Expense type");

            }

        }
        throw ResourceNotFoundException.createWith("Expense type could not be found!");
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
