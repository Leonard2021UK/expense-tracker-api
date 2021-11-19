package com.codercampus.api.service.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.repository.resource.ExpenseTrackerRepo;
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

    public ExpenseService(UserService userService, ExpenseTrackerService expenseTrackerService, ExpenseRepo expenseRepo) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseRepo = expenseRepo;
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
    public Optional<Expense> createIfNotExists(Expense expense,Long expenseTrackerId){

        if(this.expenseRepo.existsByName(expense.getName())){
            return Optional.empty();
        }

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(expenseTrackerId);

        if(expenseTrackerOpt.isPresent()) {

            // read currently logged-in user into UserService
            this.userService.setSecurityContext();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            expense.setExpenseTracker(expenseTrackerOpt.get());

            expense.setCreatedBy(userDetails.getUsername());
            expense.setUpdatedBy(userDetails.getUsername());


            return Optional.of(this.save(expense));

        }
        return Optional.empty();

    }

//    /**
//     *
//     * @param name
//     * @return
//     */
//    public boolean isExists(String name){
//        return this.expenseTrackerRepo.existsByName(name);
//    }
//
//    /**
//     *
//     * @param id
//     * @return
//     */
//    public Optional<ExpenseTracker> findById(Long id){
//        return this.expenseTrackerRepo.findById(id);
//    }
//
//    /**
//     *
//     * @return
//     */
//    public List<ExpenseTracker> findAll(){
//        return this.expenseTrackerRepo.findAll();
//    }
//
//    /**
//     *
//     * @param id
//     * @return
//     */
//    public Optional<ExpenseTracker> deleteById(Long id){
//
//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerRepo.findById(id);
//
//        if(expenseTrackerOpt.isPresent()){
//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//            expenseTracker.getMainCategory().removeExpenseTracker(expenseTracker);
//            expenseTracker.getUser().removeExpenseTracker(expenseTracker);
//            return Optional.of(this.expenseTrackerRepo.save(expenseTracker));
//        }
//        return Optional.empty();
//    }
//
//    /**
//     *
//     * @param expenseTracker
//     * @param mainCategory
//     * @param user
//     * @return
//     */
//    public ExpenseTracker updatedExpenseTracker(ExpenseTracker expenseTracker, MainCategory mainCategory, User user ){
//
//        expenseTracker.setUser(user);
//        expenseTracker.setMainCategory(mainCategory);
//        expenseTracker.setUpdatedBy(user.getUsername());
//
//        return this.save(expenseTracker);
//    }
}
