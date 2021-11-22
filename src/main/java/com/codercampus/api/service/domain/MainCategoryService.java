package com.codercampus.api.service.domain;

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
    public Optional<MainCategory> createIfNotExists(MainCategory mainCategory){
        if(this.mainCategoryRepo.existsByName(mainCategory.getName())){
            return Optional.empty();
        }else{
            // read currently logged-in user into UserService
            this.userService.setSecurityContext();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            mainCategory.setCreatedBy(userDetails.getUsername());
            mainCategory.setUpdatedBy(userDetails.getUsername());

            userDetails.getUser().addMainCategory(mainCategory);
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

    /**
     *
     * @param id
     * @return
     */
    public Optional<MainCategory> findById(Long id){
        return this.mainCategoryRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<MainCategory> findAll(){
        return this.mainCategoryRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<MainCategory> deleteById(Long id){
        Optional<MainCategory> mainCategoryOpt = this.mainCategoryRepo.findById(id);

        if(mainCategoryOpt.isPresent()){
            MainCategory mainCategory = mainCategoryOpt.get();

            mainCategory.getUser().getMainCategories().remove(mainCategory);

            return Optional.of(this.save(mainCategory));
        }
        return Optional.empty();
    }

    /**
     *
     * @param mainCategory
     * @param user
     * @return
     */
    public MainCategory update(MainCategory mainCategory, User user){

        //TODO examine the way how it could be extracted from UserDetailsImpl
        mainCategory.setUser(user);

        // read currently logged in user into UserService
        this.userService.setSecurityContext();

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        mainCategory.setUpdatedBy(userDetails.getUsername());

        return this.save(mainCategory);
    }
}
