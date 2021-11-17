package com.codercampus.api.service.resource;

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

    public MainCategoryService(UserService userService,MainCategoryRepo mainCategoryRepo) {
        this.userService = userService;
        this.mainCategoryRepo = mainCategoryRepo;
    }

    public MainCategory save(MainCategory mainCategory){
        return this.mainCategoryRepo.save(mainCategory);
    }

    public Optional<MainCategory> createIfNotExists(MainCategory mainCategory){
        if(this.mainCategoryRepo.existsByName(mainCategory.getName())){
            return Optional.empty();
        }else{
            // read currently logged in user into UserService
            this.userService.setSecurityContext();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            mainCategory.setUser(userDetails.getUser());
            mainCategory.setCreatedBy(userDetails.getUsername());
            mainCategory.setUpdatedBy(userDetails.getUsername());

            userDetails.getUser().addMainCategory(mainCategory);
            return Optional.of(this.mainCategoryRepo.save(mainCategory));
        }

    }

    public boolean isExists(String name){
        return this.mainCategoryRepo.existsByName(name);
    }

    public Optional<MainCategory> findById(Long id){
        return this.mainCategoryRepo.findById(id);
    }

    public List<MainCategory> findAll(){
        return this.mainCategoryRepo.findAll();
    }

    public void deleteById(Long id){
        this.mainCategoryRepo.deleteById(id);
    }

    public MainCategory updateMainCategory(MainCategory mainCategory, User user){

        mainCategory.setUser(user);

        // read currently logged in user into UserService
        this.userService.setSecurityContext();

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        mainCategory.setUpdatedBy(userDetails.getUsername());

        return this.save(mainCategory);
    }
}
