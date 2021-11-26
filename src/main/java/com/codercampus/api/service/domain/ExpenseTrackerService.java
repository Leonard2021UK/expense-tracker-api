package com.codercampus.api.service.domain;

import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseTrackerResponseDto;
import com.codercampus.api.repository.resource.ExpenseTrackerRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseTrackerService {

    private final UserService userService;
    private final MainCategoryService mainCategoryService;
    private final ExpenseTrackerRepo expenseTrackerRepo;
    private final ExpenseTrackerMapper expenseTrackerMapper;


    public ExpenseTrackerService(
            UserService userService,
            MainCategoryService mainCategoryService,
            ExpenseTrackerRepo expenseTrackerRepo,
            ExpenseTrackerMapper expenseTrackerMapper

            ) {
        this.userService = userService;
        this.mainCategoryService = mainCategoryService;
        this.expenseTrackerRepo = expenseTrackerRepo;
        this.expenseTrackerMapper = expenseTrackerMapper;

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
    public Optional<ExpenseTracker> createIfNotExists(ExpenseTracker expenseTracker,Long mainCategoryId){

        if(this.expenseTrackerRepo.existsByName(expenseTracker.getName())){
            return Optional.empty();
        }

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(mainCategoryId);

        if(mainCategoryOpt.isPresent()) {

            MainCategory mainCategory = mainCategoryOpt.get();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            expenseTracker.setCreatedBy(userDetails.getUsername());
            expenseTracker.setUpdatedBy(userDetails.getUsername());

            mainCategory.addExpenseTracker(expenseTracker);
            userDetails.getUser().addExpenseTracker(expenseTracker);

            return Optional.of(this.save(expenseTracker));

        }
        return Optional.empty();

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
    public ResponseEntity<List<ExpenseTrackerResponseDto>> findAll(){

        return new ResponseEntity<>(this.expenseTrackerRepo.findAll()
                .stream()
                .map(expenseTrackerMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
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
            expenseTracker.getUser().removeExpenseTracker(expenseTracker);
            return Optional.of(this.expenseTrackerRepo.save(expenseTracker));
        }
        return Optional.empty();
    }

    /**
     *
     * @param expenseTracker
     * @param mainCategory
     * @param user
     * @return
     */
    public ExpenseTracker updatedExpenseTracker(ExpenseTracker expenseTracker, MainCategory mainCategory, User user ){

        expenseTracker.setUser(user);
        expenseTracker.setMainCategory(mainCategory);
        expenseTracker.setUpdatedBy(user.getUsername());

        return this.save(expenseTracker);
    }
}
