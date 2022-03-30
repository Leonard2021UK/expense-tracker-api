package com.codercampus.api.service.domain;

import com.codercampus.api.exception.ResourceAlreadyExistException;
import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.repository.resource.MainCategoryRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MainCategoryService {

    private final UserService userService;
    private final MainCategoryRepo mainCategoryRepo;


    /**
     *
     * @param userService
     * @param mainCategoryRepo
     */
    public MainCategoryService(UserService userService,MainCategoryRepo mainCategoryRepo) {
        this.userService = userService;
        this.mainCategoryRepo = mainCategoryRepo;
    }

    /**
     *
     * @param mainCategory
     * @return
     */
    public MainCategory save(MainCategory mainCategory){
        return this.mainCategoryRepo.save(mainCategory);
    }

    /**
     *
     * @param mainCategory
     * @return
     */
    public Optional<MainCategory> createIfNotExists(MainCategory mainCategory) throws ResourceAlreadyExistException {
        if(this.mainCategoryRepo.existsByName(mainCategory.getName())){
            throw ResourceAlreadyExistException.create(mainCategory.getName());
        }else{

            UserDetailsImpl userDetails = this.userService.getUserDetails();
            userDetails.getUser().addMainCategory(mainCategory);
            mainCategory.setCreatedBy(userDetails.getUsername());
            mainCategory.setUpdatedBy(userDetails.getUsername());

//            userDetails.getUser().addMainCategory(mainCategory);
            return Optional.of(this.mainCategoryRepo.save(mainCategory));
        }

    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.mainCategoryRepo.existsByName(name);
    }


    public Optional<MainCategory> findById(Long id){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.mainCategoryRepo.findByIdAndUserId(id,userDetails.getUser().getId());
    }
    /**
     *
     * @return
     */
    public List<MainCategory> findAll(){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.mainCategoryRepo.findAllByUserId(userDetails.getUser().getId());
    }

    /**
     *
     * @param id
     * @return
     */
    public MainCategory deleteById(Long id) throws ResourceHasReferenceException, ResourceNotFoundException {
        Long currentUserId = this.userService.getUserDetails().getUser().getId();
        Optional<MainCategory> mainCategoryOpt = this.mainCategoryRepo.findByIdAndUserId(id,currentUserId);

        if(mainCategoryOpt.isPresent()){
            MainCategory mainCategory = mainCategoryOpt.get();

            if(mainCategory.getExpenseTrackers().isEmpty()){
//                for(Expense expense : expenseAddress.getExpenses()){
//                    expenseAddress.removeExpense(expense);
//                }
                this.mainCategoryRepo.deleteByIdAndUserId(id,currentUserId);

                return mainCategory;
            }else{
                throw ResourceHasReferenceException.createWith("Main category");

            }
//            mainCategory.getUser().getMainCategories().remove(mainCategory);

        }
        throw ResourceNotFoundException.createWith("Main category could not be found!");

    }

    /**
     *
     * @param mainCategory
     * @return
     */
    public MainCategory update(MainCategory mainCategory){

        UserDetailsImpl userDetails = this.userService.getUserDetails();
//        mainCategory.setUser(userDetails.getUser());
        mainCategory.setUpdatedBy(userDetails.getUsername());

        return this.save(mainCategory);
    }
}
