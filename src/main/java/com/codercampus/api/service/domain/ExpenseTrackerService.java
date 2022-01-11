package com.codercampus.api.service.domain;

import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.repository.resource.ExpenseTrackerRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

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
     * @param mainCategoryId
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
        return this.expenseTrackerRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpenseTracker> deleteById(Long id){

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerRepo.findById(id);

        if(expenseTrackerOpt.isPresent()){
            ExpenseTracker expenseTracker = expenseTrackerOpt.get();

            expenseTracker.getMainCategory().removeExpenseTracker(expenseTracker);
//            expenseTracker.getUser().removeExpenseTracker(expenseTracker);

            return Optional.of(this.expenseTrackerRepo.save(expenseTracker));
        }
        return Optional.empty();
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
