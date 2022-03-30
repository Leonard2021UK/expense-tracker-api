package com.codercampus.api.service.domain;

import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.repository.resource.ExpenseTrackerRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.resource.ResourceException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseTrackerService {

    private final UserService userService;
    private final MainCategoryService mainCategoryService;
    private final ExpenseTrackerRepo expenseTrackerRepo;

    public ExpenseTrackerService(UserService userService, MainCategoryService mainCategoryService, ExpenseTrackerRepo expenseTrackerRepo) {
        this.userService = userService;
        this.mainCategoryService = mainCategoryService;
        this.expenseTrackerRepo = expenseTrackerRepo;
    }

    /**
     *
     * @param expenseTracker
     * @return
     */
    public ExpenseTracker save(ExpenseTracker expenseTracker){
        return this.expenseTrackerRepo.save(expenseTracker);
    }

    /**
     *
     * @param expenseTracker
     * @return
     */
    public Optional<ExpenseTracker> createIfNotExists(ExpenseTracker expenseTracker){

        if(this.expenseTrackerRepo.existsByName(expenseTracker.getName())){
            return Optional.empty();
        }

//        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(mainCategoryId);

//        if(mainCategoryOpt.isPresent()) {

//            MainCategory mainCategory = mainCategoryOpt.get();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            expenseTracker.setCreatedBy(userDetails.getUsername());
            expenseTracker.setUpdatedBy(userDetails.getUsername());
            expenseTracker.setUser(userDetails.getUser());

//            mainCategory.addExpenseTracker(expenseTracker);
//            userDetails.getUser().addExpenseTracker(expenseTracker);

            return Optional.of(this.save(expenseTracker));

//        }
//        return Optional.empty();

    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.expenseTrackerRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpenseTracker> findById(Long id){
        return this.expenseTrackerRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<ExpenseTracker> findAll(){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.expenseTrackerRepo.findAllByUserId(userDetails.getUser().getId());
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    public ExpenseTracker deleteById(Long id) throws ResourceException, ResourceHasReferenceException, ResourceNotFoundException {
        Long currentUserId = this.userService.getUserDetails().getUser().getId();
        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerRepo.findByIdAndUserId(id,currentUserId);
//
        if(expenseTrackerOpt.isPresent()){

            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
            if(expenseTracker.getExpenses().isEmpty()){
                expenseTracker.getMainCategory().removeExpenseTracker(expenseTracker);
                expenseTracker.getUser().removeExpenseTracker(expenseTracker);
                this.expenseTrackerRepo.deleteExpenseTrById(id,currentUserId);
                return expenseTracker;
            }else{
                throw ResourceHasReferenceException.createWith("Expense tracker");

            }
        }
        throw ResourceNotFoundException.createWith("Expense tracker could not be found!");
    }

    /**
     *
     * @param expenseTracker
     * @param mainCategory
     * @return
     */
    public ExpenseTracker updatedExpenseTracker(ExpenseTracker expenseTracker, MainCategory mainCategory){

        UserDetailsImpl userDetails = this.userService.getUserDetails();
//        expenseTracker.setUser(userDetails.getUser());
        expenseTracker.setMainCategory(mainCategory);
        expenseTracker.setUpdatedBy(userDetails.getUsername());

        return this.save(expenseTracker);
    }
}

//    Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(id);
//
//
//        if (expenseTrackerOpt.isPresent()){
//                ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//                Set<Expense> expenses = expenseTracker.getExpenses();
//        if(expenses.isEmpty()){
//        try {
//        Optional<ExpenseTracker> deletedExpenseTrackerOpt = this.expenseTrackerService.deleteById(id);
//        if(deletedExpenseTrackerOpt.isPresent()){
//        return new ResponseEntity<>(deletedExpenseTrackerOpt.get(), HttpStatus.OK);
//        }
//        return this.errorHandler.handleResourceNotDeletedError(id.toString(), null);
//
//        } catch (ResourceException e) {
//        return this.errorHandler.handleResourceNotDeletedError(id.toString(), null);
//        }
//        }else{
//        return this.errorHandler.handleResourceNotEmptyError(id.toString(), null);
//        }
//
//        }else {
//        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);
//        }
//
////
////        if(expenseTrackerOpt.isPresent()){
////            //TODO successful feedback
////            return new ResponseEntity<>(expenseTrackerMapper.toResponseDto(expenseTrackerOpt.get()), HttpStatus.OK);
////
////        }
////        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

